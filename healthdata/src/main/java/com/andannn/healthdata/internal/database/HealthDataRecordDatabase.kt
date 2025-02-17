package com.andannn.healthdata.internal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andannn.healthdata.internal.database.dao.HealthDataRecordDao
import com.andannn.healthdata.internal.database.entity.DistanceRecordEntity
import com.andannn.healthdata.internal.database.entity.HeightRecordEntity
import com.andannn.healthdata.internal.database.entity.SleepSessionRecordEntity
import com.andannn.healthdata.internal.database.entity.SpeedRecordEntity
import com.andannn.healthdata.internal.database.entity.StepsRecordEntity
import com.andannn.healthdata.internal.database.entity.WeightRecordEntity

internal object Tables {
    const val STEPS_RECORD_TABLE = "steps_record_table"
    const val SLEEP_SESSION_RECORD_TABLE = "sleep_session_record_table"
    const val HEIGHT_RECORD_TABLE = "height_record_table"
    const val WEIGHT_RECORD_TABLE = "weight_record_table"
    const val SPEED_RECORD_TABLE = "speed_record_table"
    const val DISTANCE_RECORD_TABLE = "distance_record_table"
}

@Database(
    entities = [
        StepsRecordEntity::class,
        SleepSessionRecordEntity::class,
        HeightRecordEntity::class,
        WeightRecordEntity::class,
        SpeedRecordEntity::class,
        DistanceRecordEntity::class,
    ],
    version = 1
)
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
