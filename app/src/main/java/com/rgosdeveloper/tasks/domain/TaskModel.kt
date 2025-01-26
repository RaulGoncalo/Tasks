package com.rgosdeveloper.tasks.domain

data class TaskModel (
    val description: String,
    val id: Int,
    var isDone: Boolean,
)