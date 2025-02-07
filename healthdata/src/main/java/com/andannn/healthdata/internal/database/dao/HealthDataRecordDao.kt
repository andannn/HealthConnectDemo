package com.andannn.healthdata.internal.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.entity.BaseColumn
import com.andannn.healthdata.internal.database.entity.SleepSessionRecordEntity
import com.andannn.healthdata.internal.database.entity.StepsRecordEntity

@Dao
internal interface HealthDataRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStepRecords(stepsRecord: List<StepsRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSleepRecords(sleepSessionRecord: List<SleepSessionRecordEntity>)

    @Query("""
        SELECT * FROM ${Tables.STEPS_RECORD_TABLE}
        """
    )
    suspend fun getStepRecords(): List<StepsRecordEntity>

    @Query("""
        SELECT * FROM ${Tables.SLEEP_SESSION_RECORD_TABLE}
        """
    )
    suspend fun getSleepRecords(): List<SleepSessionRecordEntity>

    @Query("""
        DELETE FROM ${Tables.STEPS_RECORD_TABLE}
        WHERE ${BaseColumn.ID} IN (:deletedRecordIds)
        """
    )
    suspend fun deleteStepRecordsByIds(deletedRecordIds: List<String>)

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
    }
}
