package com.andannn.healthdata.internal.database.entity

import androidx.health.connect.client.records.WeightRecord
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.util.toLocalDataTime
import java.time.LocalDateTime

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
    override val lastModifiedTime: LocalDateTime,
    @ColumnInfo(name = BaseColumn.RECORD_TIME)
    override val time: LocalDateTime,
    @ColumnInfo(name = WeightColumn.WEIGHT_KILOGRAMS)
    val weightKilograms: Double
) : BaseRecordEntity, InstantaneousRecordEntity

internal fun WeightRecord.toEntity() = WeightRecordEntity(
    id = metadata.id,
    dataOriginPackageName = metadata.dataOrigin.packageName,
    lastModifiedTime = toLocalDataTime(metadata.lastModifiedTime),
    time = toLocalDataTime(time),
    weightKilograms = weight.inKilograms
)