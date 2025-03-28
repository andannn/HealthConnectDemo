package com.andannn.healthdata.internal.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.entity.BaseColumn
import com.andannn.healthdata.internal.database.entity.DistanceRecordEntity
import com.andannn.healthdata.internal.database.entity.HeightRecordEntity
import com.andannn.healthdata.internal.database.entity.SleepSessionRecordEntity
import com.andannn.healthdata.internal.database.entity.SpeedRecordEntity
import com.andannn.healthdata.internal.database.entity.StepsRecordEntity
import com.andannn.healthdata.internal.database.entity.TotalCaloriesBurnedRecordEntity
import com.andannn.healthdata.internal.database.entity.WeightRecordEntity
import com.andannn.healthdata.model.SleepSessionModel

@Dao
internal interface HealthDataRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStepRecords(stepsRecord: List<StepsRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSleepRecords(sleepSessionRecord: List<SleepSessionRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHeightRecords(heightRecord: List<HeightRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeightRecords(weightRecord: List<WeightRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSpeedRecords(records: List<SpeedRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  upsertDistanceRecords(speedRecord: List<DistanceRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCaloriesRecords(caloriesRecord: List<TotalCaloriesBurnedRecordEntity>)

    @Query("""
        SELECT * FROM ${Tables.STEPS_RECORD_TABLE}
        """
    )
    suspend fun getStepRecords(): List<StepsRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.STEPS_RECORD_TABLE}
        WHERE ${BaseColumn.START_TIME} >= :startTime AND ${BaseColumn.END_TIME} <= :endTime
        """
    )
    suspend fun getStepRecordsByTimeRange(startTime: Long, endTime: Long): List<StepsRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.DISTANCE_RECORD_TABLE}
        WHERE ${BaseColumn.START_TIME} >= :startTime AND ${BaseColumn.END_TIME} <= :endTime
        """
    )
    suspend fun getDistanceRecordsByTimeRange(startTime: Long, endTime: Long): List<DistanceRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.TOTAL_CALORIES_BURNED_RECORD_TABLE}
        WHERE ${BaseColumn.START_TIME} >= :startTime AND ${BaseColumn.END_TIME} <= :endTime
        """
    )
    suspend fun getCaloriesRecordsByTimeRange(startTime: Long, endTime: Long): List<TotalCaloriesBurnedRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.HEIGHT_RECORD_TABLE}
        WHERE ${BaseColumn.RECORD_TIME} >= :startTime AND ${BaseColumn.RECORD_TIME} <= :endTime
    """)
    suspend fun getHeightRecordsByTimeRange(startTime: Long, endTime: Long): List<HeightRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.WEIGHT_RECORD_TABLE}
        WHERE ${BaseColumn.RECORD_TIME} >= :startTime AND ${BaseColumn.RECORD_TIME} <= :endTime
    """)
    suspend fun getWeightRecordsByTimeRange(startTime: Long, endTime: Long): List<WeightRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.SPEED_RECORD_TABLE}
        WHERE ${BaseColumn.START_TIME} >= :startTime AND ${BaseColumn.END_TIME} <= :endTime
    """)
    suspend fun getSpeedRecordsByTimeRange(startTime: Long, endTime: Long): List<SpeedRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.SLEEP_SESSION_RECORD_TABLE}
        WHERE ${BaseColumn.START_TIME} >= :startTime AND ${BaseColumn.END_TIME} <= :endTime
    """)
    suspend fun getSleepRecordsByTimeRange(startTime: Long, endTime: Long): List<SleepSessionRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.SLEEP_SESSION_RECORD_TABLE}
        """
    )
    suspend fun getSleepRecords(): List<SleepSessionRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.HEIGHT_RECORD_TABLE}
        ORDER BY ${BaseColumn.RECORD_TIME} DESC
        LIMIT 1;
    """)
    suspend fun getLatestHeight(): HeightRecordEntity?

    @Query("""
        SELECT * FROM ${Tables.WEIGHT_RECORD_TABLE}
        ORDER BY ${BaseColumn.RECORD_TIME} DESC
        LIMIT 1;
    """)
    suspend fun getLatestWeight(): WeightRecordEntity?

    @Query("""
        DELETE FROM ${Tables.STEPS_RECORD_TABLE}
        WHERE ${BaseColumn.ID} IN (:deletedRecordIds)
        """
    )
    suspend fun deleteStepRecordsByIds(deletedRecordIds: List<String>)

    @Query("""
        DELETE FROM ${Tables.HEIGHT_RECORD_TABLE}
        WHERE ${BaseColumn.ID} IN (:deletedRecordIds)
        """
    )
    suspend fun deleteHeightRecordsByIds(deletedRecordIds: List<String>)

    @Query("""
        DELETE FROM ${Tables.SLEEP_SESSION_RECORD_TABLE}
        WHERE ${BaseColumn.ID} IN (:deletedRecordIds)
        """
    )
    suspend fun deleteSleepRecordsByIds(deletedRecordIds: List<String>)


    @Transaction
    suspend fun deleteRecordsByIds(deletedRecordIds: List<String>) {
        deleteStepRecordsByIds(deletedRecordIds)
        deleteSleepRecordsByIds(deletedRecordIds)
        deleteHeightRecordsByIds(deletedRecordIds)
    }
}
