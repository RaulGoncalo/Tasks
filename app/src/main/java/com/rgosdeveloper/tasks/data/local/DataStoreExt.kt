package com.rgosdeveloper.tasks.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rgosdeveloper.tasks.utils.DataStoreConstants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.NAME_DATA_STORE)