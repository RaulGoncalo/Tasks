package com.rgosdeveloper.tasks.domain.usecase

import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.common.ResultValidate
import com.rgosdeveloper.tasks.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val repository: AuthRepository)  {
    suspend fun signUp(user: UserModel) : ResultState<Boolean> {
        return try {
            val name = user.name
            val email = user.email
            val password = user.password

            val resultValidateName = validateName(name)
            val resultValidateEmail = validateEmail(email)
            val resultValidatePassword = validatePassword(password)

            if(resultValidateName is ResultValidate.Error){
                return ResultState.Error(Exception(resultValidateName.message))
            }

            if(resultValidateEmail is ResultValidate.Error){
                return ResultState.Error(Exception(resultValidateEmail.message))
            }

            if(resultValidatePassword is ResultValidate.Error){
                return ResultState.Error(Exception(resultValidatePassword.message))
            }



            repository.signUp(user)
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    suspend fun singIn(user: UserModel) : ResultState<UserModel> {
        return try {
            val email = user.email
            val password = user.password

            val resultValidateEmail = validateEmail(email)
            val resultValidatePassword = validatePassword(password)

            if(resultValidateEmail is ResultValidate.Error){
                return ResultState.Error(Exception(resultValidateEmail.message))
            }

            if(resultValidatePassword is ResultValidate.Error){
                return ResultState.Error(Exception(resultValidatePassword.message))
            }

            repository.signIn(user)
        }catch (e: Exception){
            ResultState.Error(e)
        }
    }

    fun validateName(name: String): ResultValidate<Boolean> {
        return if (name.isEmpty()) {
            ResultValidate.Error("Preencha o seu nome!")
        } else {
            ResultValidate.Success(true)
        }
    }

    fun validateEmail(email: String): ResultValidate<Boolean> {
        return if (email.isEmpty()) {
            ResultValidate.Error("Preencha o seu email!")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ResultValidate.Error("Email inválido!")
        } else {
            ResultValidate.Success(true)
        }
    }

    fun validatePassword(password: String): ResultValidate<Boolean> {
        if (password.isEmpty()) {
            return ResultValidate.Error("Preencha a sua senha!")
        } else if (password.length < 6) {
            return ResultValidate.Error("A senha deve ter pelo menos 6 caracteres!")
        } else {
            return ResultValidate.Success(true)
        }
    }

    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): ResultValidate<Boolean> {
        return if (password != confirmPassword) {
            ResultValidate.Error("As senhas não coincidem!")
        } else {
            ResultValidate.Success(true)
        }
    }
}