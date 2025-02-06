package com.andannn.healthconnectdemo.worker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.andannn.healthconnectdemo.HealthConnectApiProvider
import com.andannn.healthconnectdemo.UserDataProvider
import com.andannn.healthconnectdemo.UserDataProviderImpl
import com.andannn.healthconnectdemo.api.ClientUnavailableException
import com.andannn.healthconnectdemo.api.HealthConnectAPI
import com.andannn.healthconnectdemo.api.NoPermissionException
import com.andannn.healthconnectdemo.api.RemoteApiException
import com.andannn.healthconnectdemo.api.TokenExpiredException
import com.andannn.healthconnectdemo.db.HealthDataRecordDao
import com.andannn.healthconnectdemo.db.toEntity
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

private const val TAG = "SyncScheduleWorker"

object SyncHelper {
    /**
     * Register a worker to sync data from HealthConnect API.
     * If background sync is not available, perform the sync in the foreground.
     */
    suspend fun registerSyncScheduleWorker(application: Application) {
        if (application !is HealthConnectApiProvider) {
            throw IllegalStateException("Application must implement HealthConnectApiProvider")
        }

        val api = application.healthConnectAPI
        val isBackgroundSyncAvailable = api.isBackgroundSyncAvailable()

        Log.d(TAG, "onCreate: isBackgroundSyncAvailable $isBackgroundSyncAvailable")

        if (isBackgroundSyncAvailable) {
            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<SyncScheduleWorker>(1, TimeUnit.HOURS)
                    .build()

            WorkManager.getInstance(context = application).enqueueUniquePeriodicWork(
                "read_health_connect",
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWorkRequest
            )

            // TODO: remove this line
            WorkManager.getInstance(context = application).enqueue(
                OneTimeWorkRequestBuilder<SyncScheduleWorker>().build()
            )
        } else {
            // Background reading is not available, perform read in foreground
            WorkManager.getInstance(context = application).enqueue(
                OneTimeWorkRequestBuilder<SyncScheduleWorker>().build()
            )
        }
    }
}


class SyncScheduleWorkerFactory(
    private val healthConnectAPI: HealthConnectAPI,
    private val healthDataRecordDao: HealthDataRecordDao
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
                    healthDataRecordDao
                )
            }

            else -> {
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
            }
        }
    }
}


// The record types that we are interested in syncing.
private val recordTypes: Set<KClass<out Record>> = setOf(
    StepsRecord::class,
)

internal class SyncScheduleWorker(
    appContext: Context,
    params: WorkerParameters,
    private val healthConnectAPI: HealthConnectAPI,
    private val healthDataRecordDao: HealthDataRecordDao,
) : CoroutineWorker(appContext, params) {
    private val userDataProvider: UserDataProvider by lazy {
        UserDataProviderImpl(appContext)
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: WorkStart")
        val lastSyncToken = userDataProvider.getLastSyncToken()
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
                // TODO: save changes to database.

                val deletedRecordIds = mutableListOf<String>()
                val upsertChangeMap = mutableMapOf<KClass<out Record>, List<Change>>()
                changes.forEach { change ->
                    when (change) {
                        is DeletionChange -> {
                            deletedRecordIds.add(change.recordId)
                        }

                        is UpsertionChange -> {
                            upsertChangeMap[change.record::class] =
                                upsertChangeMap.getOrPut(change.record::class) { emptyList() } + change
                        }
                    }
                }

                if (deletedRecordIds.isNotEmpty()) {
                    healthDataRecordDao.deleteRecordsByIds(deletedRecordIds)
                }

                upsertChangeMap.forEach { (recordType, changes) ->
                    when (recordType) {
                        StepsRecord::class -> {
                            val records = changes.filterIsInstance<StepsRecord>()
                            healthDataRecordDao.upsertStepRecords(
                                records.map { it.toEntity() }
                            )
                        }
                    }
                }

                userDataProvider.setSyncToken(newToken)
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
        userDataProvider.setSyncToken(token)
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