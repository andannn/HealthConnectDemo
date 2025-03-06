package com.andannn.healthdata

import com.andannn.healthdata.model.BodyMeasurementData
import com.andannn.healthdata.model.DistanceRecordModel
import com.andannn.healthdata.model.HealthData
import com.andannn.healthdata.model.HeightRecordModel
import com.andannn.healthdata.model.SleepSessionModel
import com.andannn.healthdata.model.SpeedRecordModel
import com.andannn.healthdata.model.StepRecordModel
import com.andannn.healthdata.model.TotalCaloriesBurnedRecordModel
import com.andannn.healthdata.model.WeightRecordModel
import java.time.Instant

interface HealthRepositoryProvider {
    val repository: HealthDataRepository
}

/**
 * Repository for Health Data
 */
interface HealthDataRepository {
    /**
     * Returns true if background sync is available, false otherwise
     */
    suspend fun isBackgroundSyncAvailable(): Boolean

    /**
     * Get all sleep sessions for the given time range
     */
    suspend fun getSleeps(startTime: Instant, endTime: Instant): List<SleepSessionModel>

    /**
     * Get all speed records for the given time range
     */
    suspend fun getSpeeds(startTime: Instant, endTime: Instant): List<SpeedRecordModel>

    /**
     * Get all step records for the given time range
     */
    suspend fun getSteps(startTime: Instant, endTime: Instant): List<StepRecordModel>

    /**
     * Get all height records for the given time range
     */
    suspend fun getHeights(startTime: Instant, endTime: Instant): List<HeightRecordModel>

    /**
     * Get all weight records for the given time range
     */
    suspend fun getWeights(startTime: Instant, endTime: Instant): List<WeightRecordModel>

    /**
     * Get all total calories burned records for the given time range
     */
    suspend fun getTotalCaloriesBurned(
        startTime: Instant,
        endTime: Instant
    ): List<TotalCaloriesBurnedRecordModel>

    /**
     * Get all distance records for the given time range
     */
    suspend fun getDistance(startTime: Instant, endTime: Instant): List<DistanceRecordModel>

    /**
     * Get the aggregate health data for the given time range
     */
    suspend fun getAggregateHealthData(startTime: Instant, endTime: Instant): HealthData

    /**
     * Get the latest body measurement data
     */
    suspend fun getLatestBodyMeasurementData(): BodyMeasurementData
}
