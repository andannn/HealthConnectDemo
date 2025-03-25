package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.Device.Companion.TYPE_UNKNOWN
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables

internal object TotalCaloriesBurnedRecordColumns {
    const val ENERGY_IN_CALORIE = "energy_in_calorie"
}

@Entity(tableName = Tables.TOTAL_CALORIES_BURNED_RECORD_TABLE)
internal data class TotalCaloriesBurnedRecordEntity(
    @ColumnInfo(name = BaseColumn.ID) @PrimaryKey override val id: String,
    @ColumnInfo(name = BaseColumn.DATA_ORIGIN_PACKAGE_NAME) override val dataOriginPackageName: String,
    @ColumnInfo(name = BaseColumn.LAST_MODIFIED_TIME) override val lastModifiedTime: Long,
    @ColumnInfo(name = BaseColumn.START_TIME) override val startTime: Long,
    @ColumnInfo(name = BaseColumn.END_TIME) override val endTime: Long,
    @ColumnInfo(name = TotalCaloriesBurnedRecordColumns.ENERGY_IN_CALORIE) val energyInCalorie: Double,
    @ColumnInfo(name = BaseColumn.DEVICE_TYPE) override val deviceType: Int,
) : IntervalRecordEntity, BaseRecordEntity

internal fun TotalCaloriesBurnedRecord.toEntity() = TotalCaloriesBurnedRecordEntity(
    id = metadata.id,
    deviceType = metadata.device?.type ?: TYPE_UNKNOWN,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = metadata.lastModifiedTime.toEpochMilli(),
    startTime = startTime.toEpochMilli(),
    endTime = endTime.toEpochMilli(),
    energyInCalorie = energy.inCalories
)