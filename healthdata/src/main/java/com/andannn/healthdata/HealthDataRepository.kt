package com.andannn.healthdata

import com.andannn.healthdata.model.BodyMeasurementData
import com.andannn.healthdata.model.HealthData
import java.time.Instant
import java.time.LocalDateTime

interface HealthRepositoryProvider {
    val repository: HealthDataRepository
}

interface HealthDataRepository {
    suspend fun isBackgroundSyncAvailable(): Boolean

    // Temporary method to get data for testing
    suspend fun getWeights(): List<String>

    // Temporary method to get data for testing
    suspend fun getSteps(): List<String>

    // Temporary method to get data for testing
    suspend fun getStepsByTimeRange(startTime: Instant, endTime: Instant): List<String>

    // Temporary method to get data for testing
    suspend fun getSleeps(): List<String>

    // Temporary method to get data for testing
    suspend fun getSpeeds(): List<String>

    // Temporary method to get data for testing
    suspend fun getHeights(): List<String>

    suspend fun getHealthData(startTime: Instant, endTime: Instant): HealthData

    // TODO get newest data???
    suspend fun getBodyMeasurementData(): BodyMeasurementData
}
