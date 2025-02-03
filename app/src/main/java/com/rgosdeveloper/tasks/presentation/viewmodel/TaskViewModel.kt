package com.rgosdeveloper.tasks.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgosdeveloper.tasks.domain.common.ResultState
import com.rgosdeveloper.tasks.domain.models.TaskModel
import com.rgosdeveloper.tasks.domain.usecase.TaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val useCase: TaskUseCase
) : ViewModel() {

    private val _tasks = MutableLiveData<ResultState<List<TaskModel>>>()
    val tasks: LiveData<ResultState<List<TaskModel>>> get() = _tasks

    fun getTasks(todaysDate: String, filter: String) {
        viewModelScope.launch {
            _tasks.value = ResultState.Loading
            _tasks.value = useCase.getTasks(todaysDate, filter)
        }
    }

    fun toggleTask(id: Int, todaysDate: String, filter: String) {
        viewModelScope.launch {
            _tasks.value = ResultState.Loading
            useCase.toggleTask(id)
            _tasks.value = useCase.getTasks(todaysDate, filter)
        }
    }

    fun deleteTask(id: Int, todaysDate: String, filter: String) {
        viewModelScope.launch {
            _tasks.value = ResultState.Loading
            useCase.deleteTask(id)
            _tasks.value = useCase.getTasks(todaysDate, filter)
        }
    }
}