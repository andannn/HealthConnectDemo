package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.SleepSessionRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables.SLEEP_SESSION_RECORD_TABLE
import java.time.LocalDateTime

internal object SleepSessionRecordColumn {
    const val TITLE = "sleep_session_title"
}

@Entity(tableName = SLEEP_SESSION_RECORD_TABLE)
internal data class SleepSessionRecordEntity(
    @ColumnInfo(name = BaseColumn.ID) @PrimaryKey override val id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME) override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME) override val lastModifiedTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.START_TIME) override val startTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.END_TIME) override val endTime: LocalDateTime,
    @ColumnInfo(name = SleepSessionRecordColumn.TITLE) val title: String,
) : BaseRecordEntity, IntervalRecordEntity

internal fun SleepSessionRecord.toEntity() = SleepSessionRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    startTime = toLocalDataTime(startTime, startZoneOffset),
    endTime = toLocalDataTime(endTime, endZoneOffset),
    title = title ?: "",
)
