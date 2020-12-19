package ru.hse.miem.miemcam.data.repositories

import ru.hse.miem.miemcam.data.api.MainApi
import ru.hse.miem.miemcam.domain.repositories.IAuthRepository
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val mainApi: MainApi
) : IAuthRepository {

    override fun loginToken() = mainApi.loginToken()
}