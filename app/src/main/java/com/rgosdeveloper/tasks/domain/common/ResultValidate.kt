package com.rgosdeveloper.tasks.domain.common

sealed class ResultValidate<out T> {
    data class Success<out T>(val data: T) : ResultValidate<T>()
    data class Error(val message: String) : ResultValidate<Nothing>()
}