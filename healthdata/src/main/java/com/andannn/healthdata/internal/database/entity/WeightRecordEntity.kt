package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.metadata.Device.Companion.TYPE_UNKNOWN
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables

internal object WeightColumn {
    const val WEIGHT_KILOGRAMS = "weight_kilograms"
}

@Entity(tableName = Tables.WEIGHT_RECORD_TABLE)
internal data class WeightRecordEntity(
    @PrimaryKey
    @ColumnInfo(name = BaseColumn.ID)
    override val id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME)
    override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME)
    override val lastModifiedTime: Long,
    @ColumnInfo(name = BaseColumn.RECORD_TIME)
    override val time: Long,
    @ColumnInfo(name = WeightColumn.WEIGHT_KILOGRAMS)
    val weightKilograms: Double,
    @ColumnInfo(name = BaseColumn.DEVICE_TYPE)
    override val deviceType: Int,
) : InstantaneousRecordEntity

internal fun WeightRecord.toEntity() = WeightRecordEntity(
    id = metadata.id,
    deviceType = metadata.device?.type ?: TYPE_UNKNOWN,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = metadata.lastModifiedTime.toEpochMilli(),
    time = time.toEpochMilli(),
    weightKilograms = weight.inKilograms
)