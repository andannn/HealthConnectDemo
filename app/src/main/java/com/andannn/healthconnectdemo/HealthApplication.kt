package com.andannn.healthconnectdemo

import android.app.Application
import androidx.work.Configuration
import com.andannn.healthdata.HealthDataRepository
import com.andannn.healthdata.HealthRepositoryProvider
import com.andannn.healthdata.api.HealthConnectAPI
import com.andannn.healthdata.api.buildHealthConnectAPI
import com.andannn.healthdata.buildHealthDataRepository
import com.andannn.healthdata.database.HealthDataRecordDatabase
import com.andannn.healthdata.database.buildHealthDataRecordDatabase
import com.andannn.healthdata.worker.SyncScheduleWorkerFactory
import com.andannn.healthdata.worker.SyncTokenProvider
import com.andannn.healthdata.worker.buildSyncTokenProvider

class HealthApplication : Application(), Configuration.Provider, HealthRepositoryProvider {

    private val healthConnectAPI: HealthConnectAPI by lazy {
        buildHealthConnectAPI(this)
    }

    private val healthDataRecordDatabase: HealthDataRecordDatabase by lazy {
        buildHealthDataRecordDatabase(this)
    }

    private val syncTokenProvider: SyncTokenProvider by lazy {
        buildSyncTokenProvider(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                SyncScheduleWorkerFactory(
                    healthConnectAPI,
                    healthDataRecordDatabase.getHealthDataRecordDao(),
                    syncTokenProvider
                )
            )
            .build()

    override val repository: HealthDataRepository by lazy {
        buildHealthDataRepository(
            healthConnectAPI = healthConnectAPI,
            syncTokenProvider = syncTokenProvider,
            database = healthDataRecordDatabase
        )
    }
}
