package com.andannn.healthdata.internal

import com.andannn.healthdata.HealthDataRepository
import com.andannn.healthdata.internal.api.HealthConnectAPI
import com.andannn.healthdata.internal.database.HealthDataRecordDatabase
import com.andannn.healthdata.internal.util.InstantSerializerModuleBuilder
import com.andannn.healthdata.model.HealthData
import com.andannn.healthdata.model.toModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant

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
    private val json = Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            InstantSerializerModuleBuilder()
        }
    }

    private val dao = database.getHealthDataRecordDao()

    override suspend fun isBackgroundSyncAvailable(): Boolean {
        return healthConnectAPI.isBackgroundSyncAvailable()
    }

    override suspend fun getWeights(): List<String> {
        return dao.getWeightRecords().reversed().map { json.encodeToString(it) }
    }

    override suspend fun getSteps(): List<String> {
        return dao.getStepRecords().sortedBy {
            it.startTime
        }.reversed().map { json.encodeToString(it.toModel()) }
    }

    override suspend fun getStepsByTimeRange(startTime: Instant, endTime: Instant): List<String> {
        return dao.getStepRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .reversed().map { json.encodeToString(it) }
    }

    override suspend fun getSleeps(): List<String> {
        return dao.getSleepRecords().reversed().map { it.toString() }
    }

    override suspend fun getSpeeds(): List<String> {
        return dao.getSpeedRecords().reversed().map { it.toString() }
    }

    override suspend fun getHeights(): List<String> {
        return dao.getHeightRecords().sortedBy {
            it.time
        }.reversed().map { json.encodeToString(it.toModel()) }
    }

    override suspend fun getHealthData(startTime: Instant, endTime: Instant): HealthData {
        val stepRecords =
            dao.getStepRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
        val totalSteps = stepRecords.fold(0L) { acc, stepRecord ->
            acc + stepRecord.count
        }

        val distanceRecords =
            dao.getDistanceRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
        val totalDistance = distanceRecords.fold(0.0) { acc, distanceRecord ->
            acc + distanceRecord.distanceInMeters
        }

        val caloriesRecords = dao.getCaloriesRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
        val totalCalories = caloriesRecords.fold(0.0) { acc, caloriesRecord ->
            acc + caloriesRecord.energyInCalorie
        }

        return HealthData(
            stepCount = totalSteps,
            distance = totalDistance,
            energy = totalCalories
        )
    }
}
