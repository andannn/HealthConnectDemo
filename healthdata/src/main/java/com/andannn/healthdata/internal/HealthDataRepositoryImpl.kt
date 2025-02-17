package com.andannn.healthdata.internal

import com.andannn.healthdata.HealthDataRepository
import com.andannn.healthdata.internal.api.HealthConnectAPI
import com.andannn.healthdata.internal.database.HealthDataRecordDatabase
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
    private val dao = database.getHealthDataRecordDao()

    override suspend fun isBackgroundSyncAvailable(): Boolean {
        return healthConnectAPI.isBackgroundSyncAvailable()
    }

    override suspend fun getWeights(): List<String> {
        return dao.getWeightRecords().reversed().map { it.toString() }
    }

    override suspend fun getSteps(): List<String> {
        return dao.getStepRecords().reversed().map { it.toString() }
    }

    override suspend fun getStepsByTimeRange(startTime: Instant, endTime: Instant): List<String> {
        return dao.getStepRecordsByTimeRange(startTime.toEpochMilli(), endTime.toEpochMilli())
            .reversed().map { it.toString() }
    }

    override suspend fun getSleeps(): List<String> {
        return dao.getSleepRecords().reversed().map { it.toString() }
    }

    override suspend fun getSpeeds(): List<String> {
        return dao.getSpeedRecords().reversed().map { it.toString() }
    }

    override suspend fun getHeights(): List<String> {
        return dao.getHeightRecords().reversed().map { it.toString() }
    }
}
