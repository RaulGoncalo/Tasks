package com.rgosdeveloper.tasks.domain.usecase

import com.rgosdeveloper.tasks.data.repository.UserPreferencesRepository
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.models.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend fun saveUserPreferences(user: UserModel) {
        userPreferencesRepository.saveUserPreferences(user)
    }

    suspend fun getUserPreferences(): ResultState<UserModel> {
        return userPreferencesRepository.getUserPreferences()
    }

    suspend fun clearUserPreferences() {
        userPreferencesRepository.clearUserPreferences()
    }
}