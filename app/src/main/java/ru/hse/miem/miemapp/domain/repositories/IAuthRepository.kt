package ru.hse.miem.miemapp.domain.repositories

interface IAuthRepository {
    suspend fun auth(authCode: String)
}