package com.andannn.healthdata.internal.api

import android.content.Context
import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import java.time.Instant
import kotlin.reflect.KClass

/**
 * Thrown when the client is not available.
 */
internal class ClientUnavailableException(override val message: String? = null) : Throwable()

/**
 * Thrown when the remote API call fails.
 */
internal class RemoteApiException(override val message: String? = null) : Throwable()

/**
 * Thrown when the client does not have the required permission.
 */
internal class NoPermissionException(override val message: String? = null) : Throwable()

/**
 * Thrown when the token is expired.
 */
internal class TokenExpiredException : Throwable()


/**
 * Interface for HealthConnect API.
 */
internal interface HealthConnectAPI {

    /**
     * Check if background sync is available.
     */
    suspend fun isBackgroundSyncAvailable(): Boolean


    /**
     * Get the granted permissions.
     */
    suspend fun getGrantedPermissions(): Set<String>

    /**
     * @throws RemoteApiException
     * @throws ClientUnavailableException
     */
    suspend  fun  readRecords(
        recordType: KClass<out Record>,
        startTime: Instant,
        endTime: Instant
    ): List<Record>

    /**
     * Get changes from the given token.
     * Returns the next token and the list of changes.
     *
     * @throws RemoteApiException
     * @throws ClientUnavailableException
     * @throws TokenExpiredException
     */
    suspend fun getChangesFromToken(token: String): Pair<String, List<Change>>

    /**
     * Get a changes token for the given record types.
     *
     * @throws RemoteApiException
     * @throws ClientUnavailableException
     * @throws NoPermissionException
     */
    suspend fun getChangesToken(
        recordTypes: Set<KClass<out Record>>,
        dataOriginFilters: Set<DataOrigin> = setOf()
    ): String
}

internal fun buildHealthConnectAPI(context: Context): HealthConnectAPI {
    return HealthConnectAPIImpl(context)
}