package example.com.nm.feature.home.repository

import example.com.nm.feature.api.ServerApi
import example.com.nm.feature.home.domain.HomeSource
import example.com.nm.feature.home.domain.entity.UserData
import example.com.nm.feature.home.repository.mapper.UserDataMapper
import example.com.nm.feature.pref.NmPref
import io.reactivex.Single

class HomeRepository(private val serverApi: ServerApi, private  val nmPref: NmPref): HomeSource {

    override fun getUserInfo(): Single<UserData> {
        return if (nmPref.loggedUser != null)
            Single.just(nmPref.loggedUser)
        else
            getUserInfoServer()
    }

    override fun cleanUserInfo() {
        nmPref.clean()
    }

    private fun getUserInfoServer() : Single<UserData> {
        return serverApi.getUserInfo()
            .map { UserDataMapper.map(it) }
            .doOnSuccess {
                nmPref.loggedUser = it
            }
    }
}