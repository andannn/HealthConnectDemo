package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.HeightRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.util.toLocalDataTime
import java.time.LocalDateTime

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
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME)
    override val lastModifiedTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.RECORD_TIME)
    override val time: LocalDateTime,
    @ColumnInfo(name = HeightColumn.HEIGHT_METERS)
    val heightMeters: Double
): BaseRecordEntity, InstantaneousRecordEntity

internal fun HeightRecord.toEntity() = HeightRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    time = toLocalDataTime(time),
    heightMeters = height.inMeters
)