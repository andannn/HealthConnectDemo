package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.StepsRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables.STEPS_RECORD_TABLE
import com.andannn.healthdata.internal.database.util.toLocalDataTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

internal object StepRecordColumns {
    const val COUNT = "step_count"
}

@Entity(tableName = STEPS_RECORD_TABLE)
internal data class StepsRecordEntity(
    @ColumnInfo(name = BaseColumn.ID) @PrimaryKey override val id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME) override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME) override val lastModifiedTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.START_TIME) override val startTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.END_TIME) override val endTime: LocalDateTime,
    @ColumnInfo(name = StepRecordColumns.COUNT) val count: Long,
) : BaseRecordEntity, IntervalRecordEntity

internal fun StepsRecord.toEntity() = StepsRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    startTime = toLocalDataTime(startTime, startZoneOffset),
    endTime = toLocalDataTime(endTime, endZoneOffset),
    count = count,
)
