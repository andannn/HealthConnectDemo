package com.andannn.healthdata.api

import android.content.Context
import android.os.RemoteException
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.feature.ExperimentalFeatureAvailabilityApi
import androidx.health.connect.client.permission.HealthPermission.Companion.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.io.IOException
import java.time.Instant
import kotlin.reflect.KClass

class HealthConnectAPIImpl(
    private val context: Context
) : HealthConnectAPI {
    @OptIn(ExperimentalFeatureAvailabilityApi::class)
    override suspend fun isBackgroundSyncAvailable(): Boolean {
        try {
            return withClientOrThrow(context) { client ->
                PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND in client.permissionController.getGrantedPermissions() &&
                        client.features.getFeatureStatus(
                            HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_IN_BACKGROUND
                        ) == HealthConnectFeatures.FEATURE_STATUS_AVAILABLE
            }
        } catch (e: Exception) {
            Log.d(TAG, "isBackgroundSyncAvailable: $e")
            return false
        }
    }

    override suspend fun getGrantedPermissions(): Set<String> {
        try {
            return withClientOrThrow(context) { client ->
                client.permissionController.getGrantedPermissions()
            }
        } catch (e: Exception) {
            Log.d(TAG, "isBackgroundSyncAvailable: $e")
            return emptySet()
        }
    }

    override suspend fun readStepsByTimeRange(
        startTime: Instant,
        endTime: Instant
    ) = withClientOrThrow(context) { client ->
        val response = wrapException {
            client.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
        }

        response.records.also {
            Log.d(TAG, "readStepsByTimeRange: ${it}")
        }
    }

    override suspend fun getChangesFromToken(token: String) = withClientOrThrow(context) { client ->
        Log.d(TAG, "getChangesFromToken: token $token")
        var nextChangesToken = token
        val changeList = mutableListOf<Change>()
        do {
            val response = wrapException {
                client.getChanges(nextChangesToken)
            }

            if (response.changesTokenExpired) {
                throw TokenExpiredException()
            }
            changeList.addAll(response.changes)
            nextChangesToken = response.nextChangesToken
        } while (response.hasMore)

        nextChangesToken to changeList.toList()
    }

    override suspend fun getChangesToken(
        recordTypes: Set<KClass<out Record>>,
        dataOriginFilters: Set<DataOrigin>,
    ) = withClientOrThrow(context) { client ->
        wrapException {
            client.getChangesToken(
                request = ChangesTokenRequest(
                    recordTypes = recordTypes,
                    dataOriginFilters = dataOriginFilters
                )
            )
        }
    }
}

private inline fun <T> wrapException(block: () -> T): T {
    try {
        return block()
    } catch (e: RemoteException) {
        throw RemoteApiException(e.message)
    } catch (e: IOException) {
        throw RemoteApiException(e.message)
    } catch (e: SecurityException) {
        throw NoPermissionException(e.message)
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

private val TAG = "HealthConnectAPI"

