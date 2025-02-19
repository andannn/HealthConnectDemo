package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.StepsRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class StepRecordModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val count: Long,
)

internal fun StepsRecordEntity.toModel() = StepRecordModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    startTime = Instant.ofEpochMilli(startTime),
    endTime = Instant.ofEpochMilli(endTime),
    count = count
)