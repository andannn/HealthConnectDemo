package com.andannn.healthdata.internal.database

import android.content.Context
import androidx.health.connect.client.records.WeightRecord
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andannn.healthdata.internal.database.dao.HealthDataRecordDao
import com.andannn.healthdata.internal.database.entity.HeightRecordEntity
import com.andannn.healthdata.internal.database.entity.SleepSessionRecordEntity
import com.andannn.healthdata.internal.database.entity.StepsRecordEntity
import com.andannn.healthdata.internal.database.entity.WeightRecordEntity
import com.andannn.healthdata.internal.database.util.LocalDateTimeConverter

internal object Tables {
    const val STEPS_RECORD_TABLE = "steps_record_table"
    const val SLEEP_SESSION_RECORD_TABLE = "sleep_session_record_table"
    const val HEIGHT_RECORD_TABLE = "height_record_table"
    const val WEIGHT_RECORD_TABLE = "weight_record_table"
}

@Database(
    entities = [
        StepsRecordEntity::class,
        SleepSessionRecordEntity::class,
        HeightRecordEntity::class,
        WeightRecordEntity::class
    ],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
internal abstract class  HealthDataRecordDatabase: RoomDatabase() {

    abstract fun getHealthDataRecordDao(): HealthDataRecordDao
}

internal fun buildHealthDataRecordDatabase(context: Context): HealthDataRecordDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = HealthDataRecordDatabase::class.java,
        name = "health_data_record_db"
    ).build()
}
