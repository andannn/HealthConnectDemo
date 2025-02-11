package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.SpeedRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.util.toLocalDataTime
import java.time.LocalDateTime

@Entity(tableName = Tables.SPEED_RECORD_TABLE)
internal data class SpeedRecordEntity(
    @ColumnInfo(name = BaseColumn.ID)
    @PrimaryKey override val
    id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME)
    override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME)
    override val lastModifiedTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.START_TIME)
    override val startTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.END_TIME)
    override val endTime: LocalDateTime,
) : BaseRecordEntity, IntervalRecordEntity

internal fun SpeedRecord.toEntity() = SpeedRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    startTime = toLocalDataTime(startTime, startZoneOffset),
    endTime = toLocalDataTime(endTime, endZoneOffset),
)