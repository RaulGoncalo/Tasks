package com.rgosdeveloper.tasks.domain

data class UserModel (
    val token: String,
    val name: String,
    val email: String,
    val password: String
)