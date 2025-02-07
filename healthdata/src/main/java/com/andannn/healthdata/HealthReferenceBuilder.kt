package com.andannn.healthdata

import android.content.Context
import androidx.work.WorkerFactory
import com.andannn.healthdata.internal.SyncScheduleWorkerFactory
import com.andannn.healthdata.internal.api.HealthConnectAPI
import com.andannn.healthdata.internal.api.buildHealthConnectAPI
import com.andannn.healthdata.internal.buildHealthDataRepository
import com.andannn.healthdata.internal.database.HealthDataRecordDatabase
import com.andannn.healthdata.internal.database.buildHealthDataRecordDatabase
import com.andannn.healthdata.internal.token.SyncTokenProvider
import com.andannn.healthdata.internal.token.buildSyncTokenProvider

class HealthReferenceBuilder(
    context: Context,
) {
    private val healthConnectAPI: HealthConnectAPI = buildHealthConnectAPI(context)
    private val healthDataRecordDatabase: HealthDataRecordDatabase =
        buildHealthDataRecordDatabase(context)
    private val syncTokenProvider: SyncTokenProvider = buildSyncTokenProvider(context)

    fun buildRepository(): HealthDataRepository {
        return buildHealthDataRepository(
            healthConnectAPI = healthConnectAPI,
            syncTokenProvider = syncTokenProvider,
            database = healthDataRecordDatabase
        )
    }

    fun buildWorkerFactory(): WorkerFactory {
        return SyncScheduleWorkerFactory(
            healthConnectAPI,
            healthDataRecordDatabase.getHealthDataRecordDao(),
            syncTokenProvider
        )
    }
}