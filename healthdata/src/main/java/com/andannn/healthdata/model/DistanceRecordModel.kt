package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.DistanceRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class DistanceRecordModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val distanceInMeters: Double,
    val deviceType: Int
)

internal fun DistanceRecordEntity.toModel() = DistanceRecordModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    startTime = Instant.ofEpochMilli(startTime),
    endTime = Instant.ofEpochMilli(endTime),
    distanceInMeters = distanceInMeters,
    deviceType = deviceType
)
