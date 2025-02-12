package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.util.toLocalDataTime
import java.time.LocalDateTime

object DistanceColumn {
    const val DISTANCE_IN_METERS = "distance_in_meters"
}

@Entity(tableName = Tables.DISTANCE_RECORD_TABLE)
internal data class DistanceRecordEntity(
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
    @ColumnInfo(name = DistanceColumn.DISTANCE_IN_METERS)
    val distanceInMeters: Double,
) : BaseRecordEntity, IntervalRecordEntity

internal fun DistanceRecord.toEntity() = DistanceRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    startTime = toLocalDataTime(startTime, startZoneOffset),
    endTime = toLocalDataTime(endTime, endZoneOffset),
    distanceInMeters = distance.inMeters,
)