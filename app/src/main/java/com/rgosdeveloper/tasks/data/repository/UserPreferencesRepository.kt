package com.rgosdeveloper.tasks.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.utils.DataStoreConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    val userPreferences: Flow<UserModel> = dataStore.data.map { preferences ->
        val name = preferences[stringPreferencesKey(DataStoreConstants.USER_NAME)]
        val email = preferences[stringPreferencesKey(DataStoreConstants.USER_EMAIL)]
        val token = preferences[stringPreferencesKey(DataStoreConstants.USER_TOKEN)]

        if (name != null && email != null && token != null) {
            UserModel(name, email, token,"")
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