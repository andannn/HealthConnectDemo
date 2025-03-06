package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.TotalCaloriesBurnedRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TotalCaloriesBurnedRecordModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val energyInCalorie: Double,
    val deviceType: Int
)

internal fun TotalCaloriesBurnedRecordEntity.toModel() = TotalCaloriesBurnedRecordModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    startTime = Instant.ofEpochMilli(startTime),
    endTime = Instant.ofEpochMilli(endTime),
    energyInCalorie = energyInCalorie,
    deviceType = deviceType
)