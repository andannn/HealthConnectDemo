package com.andannn.healthdata.internal.token

import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json

internal interface SyncTokenProvider {
    /**
     * Get the last sync token.
     *
     *
     * @return last sync token if the [currentPermissions] is the same as last sync token.
     */
    fun getLastSyncToken(currentPermissions: Set<String>): String?

    /**
     * Set the last sync token.
     *
     * @param token the sync token to set.
     * @param permissions the permissions that the token is associated with.
     */
    fun setSyncToken(token: String, permissions: Set<String>? = null)
}

internal fun buildSyncTokenProvider(context: Context): SyncTokenProvider {
    return SyncTokenProviderImpl(context)
}

internal class SyncTokenProviderImpl(
    context: Context
) : SyncTokenProvider {

    private val sharedPreferences = context.getSharedPreferences(
        "sync_token_shared_preferences",
        Context.MODE_PRIVATE
    )

    override fun getLastSyncToken(currentPermissions: Set<String>): String? {
        val lastPermissions = sharedPreferences.getString("permissions", null)?.let {
            Json.decodeFromString<Set<String>>(it)
        } ?: return null

        Log.d(TAG, "getLastSyncToken: lastPermissions $lastPermissions, currentPermissions $currentPermissions")
        if (lastPermissions != currentPermissions) {
            return null
        }
        return sharedPreferences.getString("sync_token", null)
    }

    override fun setSyncToken(token: String, permissions: Set<String>?) {
        Log.d(TAG, "setSyncToken: token $token, permissions $permissions")
        sharedPreferences.edit().putString("sync_token", token).apply()
        if (permissions != null) {
            sharedPreferences.edit().putString("permissions", Json.encodeToString(permissions))
                .apply()
        }
    }

    companion object {
        private const val TAG = "UserDataProvider"
    }
}