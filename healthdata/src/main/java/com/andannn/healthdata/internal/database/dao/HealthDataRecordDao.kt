package com.andannn.healthdata.internal.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andannn.healthdata.internal.database.Tables
import com.andannn.healthdata.internal.database.entity.BaseRecordEntity
import com.andannn.healthdata.internal.database.entity.StepRecordColumns
import com.andannn.healthdata.internal.database.entity.StepsRecordEntity

@Dao
interface HealthDataRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStepRecords(stepsRecord: List<StepsRecordEntity>)


    @Query("""
        SELECT * FROM ${Tables.STEPS_RECORD_TABLE}
        """
    )
    suspend fun getStepRecords(): List<StepsRecordEntity>

    @Query("""
        DELETE FROM ${Tables.STEPS_RECORD_TABLE}
        WHERE ${StepRecordColumns.ID} IN (:deletedRecordIds)
        """
    )
    suspend fun deleteStepRecordsByIds(deletedRecordIds: List<String>)

    @Transaction
    suspend fun deleteRecordsByIds(deletedRecordIds: List<String>) {
        deleteStepRecordsByIds(deletedRecordIds)
    }
}
