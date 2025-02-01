package com.rgosdeveloper.tasks.domain.repository

import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState

interface AuthRepository {
    suspend fun signUp(user: UserModel) : ResultState<Boolean>
    suspend fun signIn(user: UserModel) : ResultState<UserModel>
}