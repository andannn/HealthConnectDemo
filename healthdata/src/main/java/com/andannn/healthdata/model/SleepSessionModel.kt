package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.SleepSessionRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class SleepSessionModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val deviceType: Int
)

internal fun SleepSessionRecordEntity.toModel() = SleepSessionModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    startTime = Instant.ofEpochMilli(startTime),
    endTime = Instant.ofEpochMilli(endTime),
    deviceType = deviceType
)


