package com.andannn.healthdata.internal

import com.andannn.healthdata.HealthDataRepository
import com.andannn.healthdata.internal.api.HealthConnectAPI
import com.andannn.healthdata.internal.database.HealthDataRecordDatabase
import com.andannn.healthdata.model.BodyMeasurementData
import com.andannn.healthdata.model.DistanceRecordModel
import com.andannn.healthdata.model.HealthData
import com.andannn.healthdata.model.HeightRecordModel
import com.andannn.healthdata.model.SleepSessionModel
import com.andannn.healthdata.model.SpeedRecordModel
import com.andannn.healthdata.model.StepRecordModel
import com.andannn.healthdata.model.TotalCaloriesBurnedRecordModel
import com.andannn.healthdata.model.WeightRecordModel
import com.andannn.healthdata.model.toModel
import java.time.Instant
import kotlin.math.pow

internal fun buildHealthDataRepository(
    healthConnectAPI: HealthConnectAPI,
    database: HealthDataRecordDatabase
): HealthDataRepository {
    return HealthDataRepositoryImpl(healthConnectAPI, database)
}

internal class HealthDataRepositoryImpl(
    private val healthConnectAPI: HealthConnectAPI,
    private val database: HealthDataRecordDatabase
) : HealthDataRepository {

    private val dao = database.getHealthDataRecordDao()

    override suspend fun isBackgroundSyncAvailable(): Boolean {
        return healthConnectAPI.isBackgroundSyncAvailable()
    }

    override suspend fun getSleeps(startTime: Instant, endTime: Instant): List<SleepSessionModel> {
        return dao.getSleepRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .map { it.toModel() }
    }

    override suspend fun getSpeeds(startTime: Instant, endTime: Instant): List<SpeedRecordModel> {
        return dao.getSpeedRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .map { it.toModel() }
    }

    override suspend fun getHeights(startTime: Instant, endTime: Instant): List<HeightRecordModel> {
        return dao.getHeightRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .map { it.toModel() }
    }

    override suspend fun getWeights(startTime: Instant, endTime: Instant): List<WeightRecordModel> {
        return dao.getWeightRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .map { it.toModel() }
    }

    override suspend fun getTotalCaloriesBurned(
        startTime: Instant,
        endTime: Instant
    ): List<TotalCaloriesBurnedRecordModel> {
        return dao.getCaloriesRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .reversed().map { it.toModel() }
    }

    override suspend fun getDistance(
        startTime: Instant,
        endTime: Instant
    ): List<DistanceRecordModel> {
        return dao.getDistanceRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .map { it.toModel() }
    }

    override suspend fun getSteps(startTime: Instant, endTime: Instant): List<StepRecordModel> {
        return dao.getStepRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .map { it.toModel() }
    }

    override suspend fun getAggravatedHealthData(startTime: Instant, endTime: Instant): HealthData {
        val stepRecords =
            dao.getStepRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
        val totalSteps =
            with(stepRecords) { if (isEmpty()) null else sumOf { it.count } }

        val distanceRecords =
            dao.getDistanceRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
        val totalDistance =
            with(distanceRecords) { if (isEmpty()) null else sumOf { it.distanceInMeters } }

        val caloriesRecords =
            dao.getCaloriesRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
        val totalCalories =
            with(caloriesRecords) { if (isEmpty()) null else sumOf { it.energyInCalorie } }

        return HealthData(
            stepCount = totalSteps,
            distance = totalDistance,
            energy = totalCalories
        )
    }

    override suspend fun getLatestBodyMeasurementData(): BodyMeasurementData {
        val height = dao.getLatestHeight()?.heightMeters
        val weight = dao.getLatestWeight()?.weightKilograms

        var bmi: Double? = null
        if (height != null && height != 0.0 && weight != null) {
            bmi = weight.div(height.pow(2))
        }

        return BodyMeasurementData(
            height = height,
            weight = weight,
            bmi = bmi
        )
    }
}
