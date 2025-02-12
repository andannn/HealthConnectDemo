package com.andannn.healthdata

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.andannn.healthdata.internal.SyncScheduleWorker
import java.util.concurrent.TimeUnit

private const val TAG = "SyncScheduleWorker"

object DataSyncHelper {

    /**
     * Perform a one-time sync of data from HealthConnect API.
     */
    fun doOneTimeSync(application: Application) {
        WorkManager.getInstance(context = application).enqueue(
            OneTimeWorkRequestBuilder<SyncScheduleWorker>().build()
        )
    }

    /**
     * Register a worker to sync data from HealthConnect API.
     * If background sync is not available, perform the sync in the foreground.
     */
    suspend fun registerSyncScheduleWorker(application: Application) {
        if (application !is HealthRepositoryProvider) {
            throw IllegalStateException("Application must implement HealthConnectApiProvider")
        }

        val isBackgroundSyncAvailable = application.repository.isBackgroundSyncAvailable()

        Log.d(TAG, "onCreate: isBackgroundSyncAvailable $isBackgroundSyncAvailable")

        if (isBackgroundSyncAvailable) {
            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<SyncScheduleWorker>(1, TimeUnit.HOURS)
                    .build()

            WorkManager.getInstance(context = application).enqueueUniquePeriodicWork(
                "read_health_connect",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )

            // TODO: remove this line
//            WorkManager.getInstance(context = application).enqueue(
//                OneTimeWorkRequestBuilder<SyncScheduleWorker>().build()
//            )
        } else {
            // Background reading is not available, perform read in foreground
            WorkManager.getInstance(context = application).enqueue(
                OneTimeWorkRequestBuilder<SyncScheduleWorker>().build()
            )
        }
    }
}


