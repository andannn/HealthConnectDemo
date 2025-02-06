package com.andannn.healthconnectdemo

import android.content.Context
import android.os.RemoteException
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.io.IOException
import java.time.Instant

private const val TAG = "HealthConnectAPI"

class ClientUnavailableException(override val message: String? = null) : Throwable()
class RemoteApiFailedException(override val message: String? = null) : Throwable()

interface HealthConnectAPI {
    suspend fun foo()

    suspend fun getGrantedPermissions(): Set<String>

    /**
     * @throws RemoteApiFailedException
     * @throws ClientUnavailableException
     */
    suspend fun readStepsByTimeRange(
        startTime: Instant,
        endTime: Instant
    ) : List<StepsRecord>
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
        val response = runCatchingRemoteApiErrors {
            client.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
        }

        response.records
    }

    private inline fun <T> runCatchingRemoteApiErrors(block: () -> T): T {
        try {
            return block()
        } catch (e: RemoteException) {
            throw RemoteApiFailedException(e.message)
        } catch (e: IOException) {
            throw RemoteApiFailedException(e.message)
        } catch (e: SecurityException) {
            throw RemoteApiFailedException(e.message)
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
        throw ClientUnavailableException("SDK_UNAVAILABLE")
    }
    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
        throw ClientUnavailableException("SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED")
    }
    Log.d(TAG, "checkIfHealthConnectInstalled: available")
    val healthConnectClient = HealthConnectClient.getOrCreate(context)
    return block(healthConnectClient)
}