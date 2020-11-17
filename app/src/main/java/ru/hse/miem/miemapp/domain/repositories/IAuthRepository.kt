package ru.hse.miem.miemapp.domain.repositories

import io.reactivex.Completable

interface IAuthRepository {
    fun auth(authCode: String): Completable
}