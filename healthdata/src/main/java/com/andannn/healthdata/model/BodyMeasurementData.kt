package com.andannn.healthdata.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BodyMeasurementData(
    @SerialName("weight") val weight: Double? = null,
    @SerialName("height") val height: Double? = null,
    @SerialName("bmi") val bmi: Double? = null,
)