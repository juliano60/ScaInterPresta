package com.nanoporetech.scainternew.ui.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nanoporetech.scainternew.network.Credentials

class CredentialsStore(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun save(username: String, password: String) {
        prefs.edit()
            .putString("username", username)
            .putString("password", password)
            .putBoolean("rememberMe", true)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun load(): Credentials? {
        val remember = prefs.getBoolean("rememberMe", false)
        if (!remember) return null
        val username = prefs.getString("username", null) ?: return null
        val password = prefs.getString("password", null) ?: return null
        return Credentials(username, password)
    }
}