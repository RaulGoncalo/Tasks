package com.rgosdeveloper.tasks.domain.models

data class TaskModel (
    val id: Int,
    val desc: String,
    var estimateAt: String,
    var doneAt: String?,
    val userId: Int
)