package com.rgosdeveloper.tasks.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.utils.DataStoreConstants
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveUserPreferences(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(DataStoreConstants.USER_NAME)] = user.name
            preferences[stringPreferencesKey(DataStoreConstants.USER_EMAIL)] = user.email
            preferences[stringPreferencesKey(DataStoreConstants.USER_TOKEN)] = user.token
        }
    }

    // Função assíncrona para obter as preferências do usuário
    suspend fun getUserPreferences(): UserModel {
        val preferences = dataStore.data.first() // Usando first() para capturar o primeiro valor emitido
        val name = preferences[stringPreferencesKey(DataStoreConstants.USER_NAME)]
        val email = preferences[stringPreferencesKey(DataStoreConstants.USER_EMAIL)]
        val token = preferences[stringPreferencesKey(DataStoreConstants.USER_TOKEN)]

        return if (name != null && email != null && token != null) {
            UserModel(name, email, token, "")
        } else {
            UserModel("", "", "", "")
        }
    }

    suspend fun clearUserPreferences() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(DataStoreConstants.USER_NAME))
            preferences.remove(stringPreferencesKey(DataStoreConstants.USER_EMAIL))
            preferences.remove(stringPreferencesKey(DataStoreConstants.USER_TOKEN))
        }
    }
}