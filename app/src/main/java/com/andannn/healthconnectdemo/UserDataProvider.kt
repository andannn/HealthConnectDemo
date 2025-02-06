package com.andannn.healthconnectdemo

import android.content.Context
import android.util.Log

interface UserDataProvider {
    fun getLastSyncToken(): String?

    fun setSyncToken(token: String)
}

private const val TAG = "UserDataProvider"
class UserDataProviderImpl(
    context: Context
) : UserDataProvider {

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
}