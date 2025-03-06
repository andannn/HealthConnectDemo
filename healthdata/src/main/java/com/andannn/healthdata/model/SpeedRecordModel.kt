package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.SpeedRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SpeedRecordModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val speedMetersPerSecond: Double,
    val deviceType: Int
)

internal fun SpeedRecordEntity.toModel() = SpeedRecordModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    startTime = Instant.ofEpochMilli(startTime),
    endTime = Instant.ofEpochMilli(endTime),
    speedMetersPerSecond = speedMetersPerSecond,
    deviceType = deviceType
)

