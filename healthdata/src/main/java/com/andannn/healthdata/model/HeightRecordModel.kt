package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.HeightRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class HeightRecordModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val time: Instant,
    val heightMeters: Double
)

internal fun HeightRecordEntity.toModel() = HeightRecordModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    time = Instant.ofEpochMilli(time),
    heightMeters = heightMeters
)