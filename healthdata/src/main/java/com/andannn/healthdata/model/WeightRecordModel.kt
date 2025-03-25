package com.andannn.healthdata.model

import com.andannn.healthdata.internal.database.entity.WeightRecordEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class WeightRecordModel(
    val id: String,
    val dataOriginPackageName: String,
    @Contextual val time: Instant,
    val weightKilograms: Double,
    val deviceType: Int
)

internal fun WeightRecordEntity.toModel() = WeightRecordModel(
    id = id,
    dataOriginPackageName = dataOriginPackageName,
    time = Instant.ofEpochMilli(time),
    weightKilograms = weightKilograms,
    deviceType = deviceType
)