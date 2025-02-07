package com.andannn.healthdata

interface HealthRepositoryProvider {
    val repository: HealthDataRepository
}

interface HealthDataRepository {
    suspend fun isBackgroundSyncAvailable(): Boolean
}
