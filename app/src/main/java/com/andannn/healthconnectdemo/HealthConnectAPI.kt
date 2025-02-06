package com.andannn.healthconnectdemo

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

private const val TAG = "HealthConnectAPI"

interface HealthConnectAPI {
    suspend fun foo()

    suspend fun getGrantedPermissions(): Set<String>

    suspend fun readStepsByTimeRange(
        startTime: Instant,
        endTime: Instant
    )
}

class HealthConnectAPIImpl(
    private val context: Context
) : HealthConnectAPI {

    override suspend fun foo() = withClientOrThrow(context) {
        Log.d(TAG, "foo: ")
        Unit
    }

    override suspend fun getGrantedPermissions() = withClientOrThrow(context) { client ->
        client.permissionController.getGrantedPermissions()
    }

    override suspend fun readStepsByTimeRange(
        startTime: Instant,
        endTime: Instant
    ) = withClientOrThrow(context) { client ->
        Log.d(TAG, "readStepsByTimeRange: $startTime, $endTime")
        val response =
            client.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
        for (stepRecord in response.records) {
            // Process each step record
            Log.d(TAG, "readStepsByTimeRange:response  ${stepRecord}")
        }
    }
}

private suspend fun <T> withClientOrThrow(
    context: Context,
    block: suspend (HealthConnectClient) -> T
): T {
    val providerPackageName = "com.google.android.apps.healthdata"
    val availabilityStatus = HealthConnectClient.getSdkStatus(context, providerPackageName)
    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
        Log.d(TAG, "checkIfHealthConnectInstalled: SDK_UNAVAILABLE")
        throw IllegalStateException("SDK_UNAVAILABLE")
    }
    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
//        Log.d(TAG, "checkIfHealthConnectInstalled: SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED")
//        // Optionally redirect to package installer to find a provider, for example:
//        val uriString =
//            "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
//        context.startActivity(
//            Intent(Intent.ACTION_VIEW).apply {
//                setPackage("com.android.vending")
//                data = Uri.parse(uriString)
//                putExtra("overlay", true)
//                putExtra("callerId", context.packageName)
//            }
//        )
        throw IllegalStateException("SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED")
    }
    Log.d(TAG, "checkIfHealthConnectInstalled: available")
    val healthConnectClient = HealthConnectClient.getOrCreate(context)
    return block(healthConnectClient)
}