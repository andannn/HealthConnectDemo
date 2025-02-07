package com.andannn.healthdata.internal

import com.andannn.healthdata.HealthDataRepository
import com.andannn.healthdata.internal.api.HealthConnectAPI
import com.andannn.healthdata.internal.database.HealthDataRecordDatabase
import com.andannn.healthdata.internal.token.SyncTokenProvider


internal fun buildHealthDataRepository(
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
