package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.StepsRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables.STEPS_RECORD_TABLE
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object StepRecordColumns {
    const val ID = "step_record_id"
    const val DATA_ORIGIN_PACKAGE_NAME = "data_origin_package_name"
    const val LAST_MODIFIED_TIME = "last_modified_time"
    const val START_TIME = "start_time"
    const val END_TIME = "end_time"
    const val COUNT = "count"
}

@Entity(tableName = STEPS_RECORD_TABLE)
data class StepsRecordEntity(
    @ColumnInfo(name = StepRecordColumns.ID) @PrimaryKey override val id: String,
    @ColumnInfo(name = StepRecordColumns.DATA_ORIGIN_PACKAGE_NAME) override val dataOriginPackageName: String,
    @ColumnInfo(name = StepRecordColumns.LAST_MODIFIED_TIME) override val lastModifiedTime: LocalDateTime,
    @ColumnInfo(name = StepRecordColumns.START_TIME) override val startTime: LocalDateTime,
    @ColumnInfo(name = StepRecordColumns.END_TIME) override val endTime: LocalDateTime,
    @ColumnInfo(name = StepRecordColumns.COUNT) val count: Long,
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