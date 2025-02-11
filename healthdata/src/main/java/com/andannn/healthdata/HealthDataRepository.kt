package com.andannn.healthdata

interface HealthRepositoryProvider {
    val repository: HealthDataRepository
}

interface HealthDataRepository {
    suspend fun isBackgroundSyncAvailable(): Boolean

    suspend fun getWeights(): List<String>

    suspend fun getSteps(): List<String>

    suspend fun getSleeps(): List<String>

    suspend fun getSpeeds(): List<String>

    suspend fun getHeights(): List<String>
}
