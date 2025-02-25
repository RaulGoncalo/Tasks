package com.rgosdeveloper.tasks.data.repository

import android.util.Log
import com.rgosdeveloper.tasks.data.remote.ApiService
import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.models.SignInModel

import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun signUp(user: UserModel): ResultState<Boolean> {
        var messageErro = ""
        try {
            val response = apiService.signUp(user)

            if (response.isSuccessful && response.code() == 204) {
                Log.i("app_tasks", "cadastrou")
                return ResultState.Success(true)
            } else if (response.code() == 400) {
                messageErro = "Usuário já cadastrado"
            } else {
                messageErro = "Erro ao cadastrar usuário"
            }
        } catch (e: Exception) {
            Log.i("app_tasks", "Error: $e")
            messageErro = e.message.toString()
        }
        return ResultState.Error(Exception(messageErro))
    }

    suspend fun signIn(email: String, password: String): ResultState<UserModel> {
        var messageErro = ""
        try {
            val response = apiService.signIn(SignInModel(email, password))

            if (response.isSuccessful && response.code() == 200) {
                val user = response.body()
                if (user != null) {
                    Log.i("app_tasks", "logado")
                    return ResultState.Success(user)
                } else {
                    messageErro = "Usuário é nulo"
                }
            } else if ((response.code() == 400 || response.code() == 401) && response.errorBody() != null) {
                messageErro = "Informações inválidas"
            } else {
                messageErro = "Erro ao autenticar usuário"
            }
        } catch (e: Exception) {
            Log.i("app_tasks", "Error: $e")
            messageErro = e.message.toString()
        }
        return ResultState.Error(Exception(messageErro))
    }
}