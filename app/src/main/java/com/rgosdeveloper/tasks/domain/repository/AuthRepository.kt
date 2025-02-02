package com.rgosdeveloper.tasks.domain.repository

import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState

interface AuthRepository {
    suspend fun signUp(user: UserModel) : ResultState<Boolean>
    suspend fun signIn(email: String, password: String) : ResultState<UserModel>
}