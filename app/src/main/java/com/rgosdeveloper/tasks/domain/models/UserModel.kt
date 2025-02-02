package com.rgosdeveloper.tasks.domain.models

data class UserModel (
    val token: String,
    val name: String,
    val email: String,
    val password: String
)