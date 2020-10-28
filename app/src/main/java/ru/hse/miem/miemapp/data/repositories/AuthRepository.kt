package ru.hse.miem.miemapp.data.repositories

import ru.hse.miem.miemapp.data.api.AuthRequest
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val cabinetApi: CabinetApi
) : IAuthRepository {

    override fun auth(authCode: String) = cabinetApi.auth(AuthRequest(authCode))
            .map { it.data.token }

}