package com.andannn.healthconnectdemo

import android.app.Application
import androidx.work.Configuration
import com.andannn.healthconnectdemo.api.HealthConnectAPI
import com.andannn.healthconnectdemo.api.HealthConnectAPIImpl
import com.andannn.healthconnectdemo.db.HealthDataRecordDao
import com.andannn.healthconnectdemo.db.HealthDataRecordDaoImpl
import com.andannn.healthconnectdemo.worker.SyncScheduleWorkerFactory

interface HealthConnectApiProvider {
    val healthConnectAPI: HealthConnectAPI

    val healthDataRecordDao: HealthDataRecordDao
}

class HealthApplication : Application(), Configuration.Provider, HealthConnectApiProvider {

    override val healthConnectAPI: HealthConnectAPI by lazy {
        HealthConnectAPIImpl(this)
    }
    override val healthDataRecordDao: HealthDataRecordDao by lazy {
        HealthDataRecordDaoImpl(this)
    }

    companion object {
        private var sInstance: HealthApplication? = null
        fun getInstance(): HealthApplication {
            return sInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                SyncScheduleWorkerFactory(healthConnectAPI, healthDataRecordDao)
            )
            .build()
}
