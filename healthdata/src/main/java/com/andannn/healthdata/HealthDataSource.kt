package com.andannn.healthdata

import com.andannn.healthdata.api.HealthConnectAPI
import com.andannn.healthdata.database.HealthDataRecordDatabase
import com.andannn.healthdata.worker.SyncTokenProvider

interface HealthDataRepository {
    suspend fun isBackgroundSyncAvailable(): Boolean
}

fun buildHealthDataRepository(
    healthConnectAPI: HealthConnectAPI,
    syncTokenProvider: SyncTokenProvider,
    database: HealthDataRecordDatabase
): HealthDataRepository {
    return HealthDataRepositoryImpl(healthConnectAPI, syncTokenProvider, database)
}

internal class HealthDataRepositoryImpl(
    private val healthConnectAPI: HealthConnectAPI,
    private val syncTokenProvider: SyncTokenProvider,
    private val database: HealthDataRecordDatabase
) : HealthDataRepository {
    override suspend fun isBackgroundSyncAvailable(): Boolean {
        return healthConnectAPI.isBackgroundSyncAvailable()
    }
}
