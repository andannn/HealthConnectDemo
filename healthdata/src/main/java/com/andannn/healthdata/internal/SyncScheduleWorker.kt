package com.andannn.healthdata.internal

import android.content.Context
import android.util.Log
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.andannn.healthdata.internal.api.ClientUnavailableException
import com.andannn.healthdata.internal.api.HealthConnectAPI
import com.andannn.healthdata.internal.api.NoPermissionException
import com.andannn.healthdata.internal.api.RemoteApiException
import com.andannn.healthdata.internal.api.TokenExpiredException
import com.andannn.healthdata.internal.database.HealthDataRecordDao
import com.andannn.healthdata.internal.database.toEntity
import com.andannn.healthdata.internal.token.SyncTokenProvider
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.reflect.KClass

private const val TAG = "SyncScheduleWorker"

// The record types that we are interested in syncing.
private val recordTypes: Set<KClass<out Record>> = setOf(
    StepsRecord::class,
)

internal class SyncScheduleWorker(
    appContext: Context,
    params: WorkerParameters,
    private val healthConnectAPI: HealthConnectAPI,
    private val healthDataRecordDao: HealthDataRecordDao,
    private val syncTokenProvider: SyncTokenProvider
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: WorkStart")
        val lastSyncToken = syncTokenProvider.getLastSyncToken()
        try {
            if (lastSyncToken == null) {
                Log.d(TAG, "doWork: LastSyncToken is null, do initial sync")
                doInitialSync()
                createNewTokenAndSave()
                return Result.success()
            }

            try {
                Log.d(TAG, "getChangesFromToken: changes E token: $lastSyncToken")
                val (newToken, changes) = healthConnectAPI.getChangesFromToken(lastSyncToken)
                Log.d(TAG, "getChangesFromToken: changes X changes $changes")

                val deletedRecordIds = mutableListOf<String>()
                val upsertChangeMap = mutableMapOf<KClass<out Record>, List<Record>>()
                changes.forEach { change ->
                    when (change) {
                        is DeletionChange -> {
                            Log.d(TAG, "doWork: DeletionChange ${change.recordId}")
                            deletedRecordIds.add(change.recordId)
                        }

                        is UpsertionChange -> {
                            Log.d(TAG, "doWork: UpsertionChange ${change.record}")
                            upsertChangeMap[change.record::class] =
                                upsertChangeMap.getOrPut(change.record::class) { emptyList() } + change.record
                        }
                    }
                }

                if (deletedRecordIds.isNotEmpty()) {
                    healthDataRecordDao.deleteRecordsByIds(deletedRecordIds)
                }

                upsertChangeMap.forEach { (recordType, records) ->
                    when (recordType) {
                        StepsRecord::class -> {
                            healthDataRecordDao.upsertStepRecords(
                                records.filterIsInstance<StepsRecord>().map { it.toEntity() }
                            )
                        }
                    }
                }

                syncTokenProvider.setSyncToken(newToken)
            } catch (e: TokenExpiredException) {
                Log.d(TAG, "token is invalid!, clear db and do initial sync $e")
                doInitialSync()
                createNewTokenAndSave()
                return Result.success()
            }

            return Result.success()
        } catch (e: ClientUnavailableException) {
            Log.e(TAG, "$e")
            // current platform does not support HealthConnect, so we can't sync.
            return Result.failure()
        } catch (e: RemoteApiException) {
            Log.e(TAG, "$e")
            // error communicating with the HealthConnect API.
            return Result.retry()
        } catch (e: NoPermissionException) {
            Log.e(TAG, "$e")
            // user has revoked permissions, so we can't sync.
            return Result.failure()
        }
    }

    private suspend fun createNewTokenAndSave() {
        val token = healthConnectAPI.getChangesToken(recordTypes)
        syncTokenProvider.setSyncToken(token)
    }

    private suspend fun doInitialSync() {
        // TODO: clear database.

        recordTypes.forEach {
            try {
                when (it) {
                    StepsRecord::class -> syncStepRecord()
                }
            } catch (permissionException: NoPermissionException) {
                // Ignore the record type if the user has revoked permission.
            }
        }
    }

    private suspend fun syncStepRecord() {
        val record = healthConnectAPI.readStepsByTimeRange(
            startTime = Instant.now().minus(1, ChronoUnit.DAYS),
            endTime = Instant.now()
        )

        healthDataRecordDao.upsertStepRecords(
            record.map { it.toEntity() }
        )
    }
}

internal class SyncScheduleWorkerFactory internal constructor(
    private val healthConnectAPI: HealthConnectAPI,
    private val healthDataRecordDao: HealthDataRecordDao,
    private val syncTokenProvider: SyncTokenProvider
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            SyncScheduleWorker::class.java.name -> {
                SyncScheduleWorker(
                    appContext,
                    workerParameters,
                    healthConnectAPI,
                    healthDataRecordDao,
                    syncTokenProvider
                )
            }

            else -> {
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
            }
        }
    }
}
