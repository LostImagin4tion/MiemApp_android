package ru.hse.miem.miemcam.domain.repositories

import io.reactivex.Completable

interface IAuthRepository {
    fun loginToken(): Completable
}