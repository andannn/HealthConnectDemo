package com.andannn.healthdata.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class HealthData(
    @SerialName("stepCount") val stepCount: Long? = null,
    @SerialName("distance") val distance: Double? = null,
    @SerialName("energy") val energy: Double? = null,
    @SerialName("heartPoint") val heartPoint: Double? = null,
    @SerialName("speed") val speed: Double? = null,
    @SerialName("normalExercise") val normalExercise: Double? = null,
)