package ru.hse.miem.miemapp.data.api

data class AuthResponse(
    val data: AuthResponseData
)

data class AuthResponseData(
    val email: String,
    val full_name: String,
    val token: String
)