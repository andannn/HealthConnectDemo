package com.andannn.healthdata.internal.database

import androidx.health.connect.client.records.StepsRecord
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

data class StepsRecordEntity(
    override val id: String,
    override val dataOriginPackageName: String,
    override val lastModifiedTime: LocalDateTime,
    override val startTime: LocalDateTime,
    override val endTime: LocalDateTime,
    val count: Long,
) : BaseRecordEntity, IntervalRecordEntity

fun StepsRecord.toEntity() = StepsRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    startTime = toLocalDataTime(startTime, startZoneOffset),
    endTime = toLocalDataTime(endTime, endZoneOffset),
    count = count,
)

fun toLocalDataTime(
    instant: Instant, zoneOffset: ZoneOffset? = null
): LocalDateTime = LocalDateTime.ofInstant(instant, zoneOffset ?: ZoneId.systemDefault())