package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.DistanceRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables

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
    override val lastModifiedTime: Long,
    @ColumnInfo(name = BaseColumn.START_TIME)
    override val startTime: Long,
    @ColumnInfo(name = BaseColumn.END_TIME)
    override val endTime: Long,
    @ColumnInfo(name = DistanceColumn.DISTANCE_IN_METERS)
    val distanceInMeters: Double,
) : BaseRecordEntity, IntervalRecordEntity

internal fun DistanceRecord.toEntity() = DistanceRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = metadata.lastModifiedTime.toEpochMilli(),
    startTime = startTime.toEpochMilli(),
    endTime = endTime.toEpochMilli(),
    distanceInMeters = distance.inMeters,
)