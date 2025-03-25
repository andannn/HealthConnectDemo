package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.metadata.Device.Companion.TYPE_UNKNOWN
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables

@Entity(tableName = Tables.SPEED_RECORD_TABLE)
internal data class SpeedRecordEntity(
    @ColumnInfo(name = BaseColumn.ID)
    @PrimaryKey override val
    id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME)
    override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME)
    override val lastModifiedTime: Long,
    @ColumnInfo(name = BaseColumn.START_TIME)
    override val startTime: Long,
    @ColumnInfo(name = BaseColumn.END_TIME)
    override val endTime: Long,
    @ColumnInfo(name = BaseColumn.DEVICE_TYPE)
    override val deviceType: Int,
    val speedMetersPerSecond: Double,
) : IntervalRecordEntity

internal fun SpeedRecord.toEntity() = SpeedRecordEntity(
    id = metadata.id,
    deviceType = metadata.device?.type ?: TYPE_UNKNOWN,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = metadata.lastModifiedTime.toEpochMilli(),
    startTime = startTime.toEpochMilli(),
    endTime = endTime.toEpochMilli(),
    speedMetersPerSecond = samples.getOrNull(0)?.speed?.inMetersPerSecond ?: 0.0
)