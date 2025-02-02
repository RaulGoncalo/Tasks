package com.rgosdeveloper.tasks.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgosdeveloper.tasks.domain.models.UserModel
import com.rgosdeveloper.tasks.domain.usecase.UserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesUseCase: UserPreferencesUseCase
) : ViewModel() {

    private val _userPreferences = MutableLiveData<UserModel>()
    val userPreferences: LiveData<UserModel> get() = _userPreferences

    fun saveUserPreferences(user: UserModel){
        viewModelScope.launch {
            userPreferencesUseCase.saveUserPreferences(user)
        }
    }

    fun getUserPreferences(){
        viewModelScope.launch {
            val user = userPreferencesUseCase.getUserPreferences()
            _userPreferences.value = user
        }
    }

    fun clearUserPreferences(){
        viewModelScope.launch {
            userPreferencesUseCase.clearUserPreferences()
        }
    }
}