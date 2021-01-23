package ru.hse.miem.miemapp.data.repositories

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import ru.hse.miem.miemapp.Session
import ru.hse.miem.miemapp.data.api.AuthRequest
import ru.hse.miem.miemapp.data.api.CabinetApi
import ru.hse.miem.miemapp.domain.repositories.IAuthRepository
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val cabinetApi: CabinetApi,
    private val session: Session
) : IAuthRepository {

    override suspend fun auth(authCode: String) = withIO {
        cabinetApi.auth(AuthRequest(authCode)).data.token.let {
            session.token = it

            val payload =
                String(Base64.decode(it.split(".")[1], Base64.URL_SAFE), Charsets.UTF_8)
            Gson().fromJson(payload, TokenPayload::class.java).apply {
                session.email = email
                session.isStudent = student
                session.isStuff = staff
            }
            Log.i("Auth", session.toString())
        }
        return@withIO
    }


    private data class TokenPayload(
        val iat: Long,
        val username: String,
        val email: String,
        val exp: Long,
        val staff: Boolean,
        val student: Boolean
    )

}