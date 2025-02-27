package com.andannn.healthdata.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class HealthData(
    @SerialName("stepCount") val stepCount: Long? = null,
    @SerialName("distance") val distance: Double? = null,
    @SerialName("energy") val energy: Double? = null,

// TODO HealConnect中没有这个数据, Heart Points是 Google Fit 的一个专有概念
    @SerialName("heartPoint") val heartPoint: Double? = null,

// TODO HealConnect给的数据是某一个时间点的速度， 而不是平均速度。我该如何计算一段时间的平均速度？
    @SerialName("speed") val speed: Double? = null,

// TODO 这个是啥？
    @SerialName("normalExercise") val normalExercise: Double? = null,
)