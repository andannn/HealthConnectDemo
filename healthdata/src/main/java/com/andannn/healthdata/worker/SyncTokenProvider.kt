package com.andannn.healthdata.worker

import android.content.Context
import android.util.Log

interface SyncTokenProvider {
    fun getLastSyncToken(): String?

    fun setSyncToken(token: String)
}

fun buildSyncTokenProvider(context: Context): SyncTokenProvider {
    return SyncTokenProviderImpl(context)
}

internal class SyncTokenProviderImpl(
    context: Context
) : SyncTokenProvider {

    private val sharedPreferences = context.getSharedPreferences(
        "sync_token_shared_preferences",
        Context.MODE_PRIVATE
    )

    override fun getLastSyncToken(): String? {
        return sharedPreferences.getString("sync_token", null)
    }

    override fun setSyncToken(token: String) {
        Log.d(TAG, "setSyncToken: token $token")
        sharedPreferences.edit().putString("sync_token", token).apply()
    }

    companion object {
        private const val TAG = "UserDataProvider"
    }
}