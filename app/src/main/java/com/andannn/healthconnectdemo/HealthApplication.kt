package com.andannn.healthconnectdemo

import android.app.Application
import androidx.work.Configuration
import com.andannn.healthdata.HealthDataRepository
import com.andannn.healthdata.HealthReferenceBuilder
import com.andannn.healthdata.HealthRepositoryProvider

class HealthApplication : Application(), Configuration.Provider, HealthRepositoryProvider {

    private lateinit var healthDataSourceBuilder: HealthReferenceBuilder
    private lateinit var _repository: HealthDataRepository

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(
                healthDataSourceBuilder.buildWorkerFactory()
            )
            .build()

    override fun onCreate() {
        super.onCreate()
        healthDataSourceBuilder = HealthReferenceBuilder(this)
        _repository = healthDataSourceBuilder.buildRepository()
    }

    override val repository: HealthDataRepository
        get() = _repository
}
