package com.andannn.healthdata.database

import android.content.Context

interface HealthDataRecordDatabase {
    fun getHealthDataRecordDao(): HealthDataRecordDao
}

fun buildHealthDataRecordDatabase(context: Context): HealthDataRecordDatabase {
    return HealthDataRecordDatabaseImpl(context)
}

internal class HealthDataRecordDatabaseImpl(private  val context: Context) : HealthDataRecordDatabase {
    override fun getHealthDataRecordDao(): HealthDataRecordDao {
        return HealthDataRecordDaoImpl(context)
    }
}