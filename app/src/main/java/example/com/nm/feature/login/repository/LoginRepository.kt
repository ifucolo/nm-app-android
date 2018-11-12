package example.com.nm.feature.login.repository

import example.com.nm.feature.api.ServerApi
import example.com.nm.feature.login.domain.LoginSource
import example.com.nm.feature.login.repository.mapper.UserMapper
import example.com.nm.feature.pref.Consts
import example.com.nm.feature.pref.NmPref
import io.reactivex.Completable

class LoginRepository(private val serverApi: ServerApi, private val nmPref: NmPref) : LoginSource {

    override fun login(username: String, password: String): Completable {
        return serverApi.login(username, password, "password")
            .map { UserMapper.map(it) }
            .doOnSuccess {
                nmPref[Consts.ACCESS_TOKEN] = "Bearer ${it.accessToken}"
                nmPref[Consts.ACCESS_TOKEN_REFRESH] = "Bearer ${it.refreshToken}"
                nmPref[Consts.TOKEN_TYPE] = it.tokenType
                nmPref[Consts.TOKEN_EXPIRE_IN] = it.expiresIn
            }
            .ignoreElement()
    }

}