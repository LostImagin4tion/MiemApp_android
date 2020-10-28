package ru.hse.miem.miemapp.domain.repositories

import io.reactivex.Single

interface IAuthRepository {
    fun auth(authCode: String): Single<String>
}