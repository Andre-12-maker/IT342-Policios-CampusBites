package com.example.campusbites.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("session")

@Singleton
class SessionRepository @Inject constructor(
    @ApplicationContext private val ctx: Context,
) {
    companion object {
        val KEY_TOKEN = stringPreferencesKey("access_token")
        val KEY_USER  = stringPreferencesKey("user_json")
    }

    val token: Flow<String?> =
        ctx.dataStore.data.map { it[KEY_TOKEN] }

    val userJson: Flow<String?> =
        ctx.dataStore.data.map { it[KEY_USER] }

    suspend fun save(token: String, userJson: String) {
        ctx.dataStore.edit {
            it[KEY_TOKEN] = token
            it[KEY_USER]  = userJson
        }
    }

    suspend fun clear() {
        ctx.dataStore.edit { it.clear() }
    }
}