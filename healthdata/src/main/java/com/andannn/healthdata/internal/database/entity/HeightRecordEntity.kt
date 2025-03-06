package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.metadata.Device.Companion.TYPE_UNKNOWN
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables

internal object HeightColumn {
    const val HEIGHT_METERS = "height_meters"
}

@Entity(tableName = Tables.HEIGHT_RECORD_TABLE)
internal data class HeightRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = BaseColumn.ID)
    override val id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME)
    override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.DEVICE_TYPE)
    override val deviceType: Int,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME)
    override val lastModifiedTime: Long,
    @ColumnInfo(name = BaseColumn.RECORD_TIME)
    override val time: Long,
    @ColumnInfo(name = HeightColumn.HEIGHT_METERS)
    val heightMeters: Double
) : InstantaneousRecordEntity

internal fun HeightRecord.toEntity() = HeightRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    deviceType = metadata.device?.type ?: TYPE_UNKNOWN,
    lastModifiedTime = metadata.lastModifiedTime.toEpochMilli(),
    time = time.toEpochMilli(),
    heightMeters = height.inMeters,
)