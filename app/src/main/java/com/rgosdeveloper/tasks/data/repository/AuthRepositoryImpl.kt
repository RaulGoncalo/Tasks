package com.rgosdeveloper.tasks.data.repository

import android.util.Log
import com.rgosdeveloper.tasks.data.remote.ApiService
import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun signUp(user: UserModel) : ResultState<Boolean> {
        var messageErro = ""
        try {
            val response  = apiService.signUp(user)
            if(response.isSuccessful && response.code() == 204){
                Log.i("app_tasks", "cadastrou")
                return ResultState.Success(true)
            }

            if(response.code() == 400){
                messageErro = "Usuário já cadastrado"
            }
        }catch (e: Exception){
            Log.i("app_tasks", "Error: $e")
            messageErro = e.message.toString()
        }
        return ResultState.Error(Exception(messageErro))
    }

    override suspend fun signIn(user: UserModel): ResultState<UserModel> {
        var messageErro = ""
        try {
            val response  = apiService.signIn(user)
            if(response.isSuccessful && response.code() == 200){

                val user = response.body()
                if(user != null){
                    Log.i("app_tasks", "logado")
                    return ResultState.Success(user)
                }else{
                    messageErro = "Usuário é nulo"
                }
            }

            if(response.code() != 204 && response.errorBody() != null){
                messageErro = "Informações inválidas"
            }
        }catch (e: Exception){
            Log.i("app_tasks", "Error: $e")
            messageErro = e.message.toString()
        }
        return ResultState.Error(Exception(messageErro))
    }
}