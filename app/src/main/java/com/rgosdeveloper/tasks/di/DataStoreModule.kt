package com.rgosdeveloper.tasks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.rgosdeveloper.tasks.data.local.dataStore
import com.rgosdeveloper.tasks.data.repository.UserPreferencesRepository
import com.rgosdeveloper.tasks.domain.usecase.UserPreferencesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        //Extension Function para acessar o DataStore
        return context.dataStore
    }

    @Provides
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

    @Provides
    fun provideUserPreferencesUseCase(userPreferencesRepository: UserPreferencesRepository): UserPreferencesUseCase {
        return UserPreferencesUseCase(userPreferencesRepository)
    }

}