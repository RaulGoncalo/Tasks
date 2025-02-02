package com.rgosdeveloper.tasks.domain.models

data class TaskModel (
    val description: String,
    val id: Int,
    var isDone: Boolean,
)