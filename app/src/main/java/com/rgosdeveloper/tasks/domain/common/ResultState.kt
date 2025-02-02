package com.rgosdeveloper.tasks.domain.common

sealed class ResultState<out T>  {
    data class Success<out T> (val data: T) : ResultState<T>()
    data class Error(val exception: Exception) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}