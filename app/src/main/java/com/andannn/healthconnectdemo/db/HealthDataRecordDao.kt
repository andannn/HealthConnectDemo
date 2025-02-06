package com.andannn.healthconnectdemo.db

import android.content.Context
import android.util.Log
import java.io.File

interface HealthDataRecordDao {

    fun upsertStepRecords(stepsRecord: List<StepsRecordEntity>)

    fun deleteRecordsByIds(deletedRecordIds: List<String>)
}

class HealthDataRecordDaoImpl(
    context: Context
): HealthDataRecordDao {

    private val fileDir: File = context.filesDir
    override fun upsertStepRecords(stepsRecord: List<StepsRecordEntity>) {
        Log.d("JQN", "upsertStepRecords: stepsRecord $stepsRecord")
        stepsRecord.forEach {
            File(fileDir, "steps_record").appendText(it.toString())
            File(fileDir, "steps_record").appendText("\n")
        }
    }

    override fun deleteRecordsByIds(deletedRecordIds: List<String>) {
    }
}