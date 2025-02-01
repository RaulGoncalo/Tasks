package com.rgosdeveloper.tasks.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgosdeveloper.tasks.domain.UserModel
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.common.ResultValidate
import com.rgosdeveloper.tasks.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val useCase: AuthUseCase) : ViewModel() {
    private val _signUpState = MutableLiveData<ResultState<Boolean>>()
    val signUpState: LiveData<ResultState<Boolean>> get() = _signUpState

    private val _signInState = MutableLiveData<ResultState<UserModel>>()
    val signInState: LiveData<ResultState<UserModel>> get() = _signInState

    private val _emailState = MutableLiveData<ResultValidate<Boolean>>()
    val emailState: LiveData<ResultValidate<Boolean>> get() = _emailState

    private val _nameState = MutableLiveData<ResultValidate<Boolean>>()
    val nameState: LiveData<ResultValidate<Boolean>> get() = _nameState

    private val _passwordState = MutableLiveData<ResultValidate<Boolean>>()
    val passwordState: LiveData<ResultValidate<Boolean>> get() = _passwordState

    fun signUp(user: UserModel) {
        viewModelScope.launch {
            _signUpState.value = ResultState.Loading

            _nameState.value = useCase.validateName(user.name)
            _emailState.value = useCase.validateEmail(user.email)
            _passwordState.value = useCase.validatePassword(user.password)

            if(
                _nameState.value is ResultValidate.Success &&
                _emailState.value is ResultValidate.Success &&
                _passwordState.value is ResultValidate.Success
            ){
                _signUpState.value = useCase.signUp(user)
            }else{
                _signUpState.value = ResultState.Error(Exception("Valide os campos!"))
            }
        }
    }

    fun signIn(user: UserModel) {
        viewModelScope.launch {
            _signInState.value = ResultState.Loading
            _emailState.value = useCase.validateEmail(user.email)
            _passwordState.value = useCase.validatePassword(user.password)

            if(_emailState.value is ResultValidate.Success && _passwordState.value is ResultValidate.Success){
                _signInState.value = useCase.singIn(user)
            }else{
                _signInState.value = ResultState.Error(Exception("Valide os campos!"))
            }
        }
    }
}