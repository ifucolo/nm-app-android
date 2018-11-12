package example.com.nm.feature.home.domain

import example.com.nm.feature.home.domain.entity.UserData
import io.reactivex.Single

interface HomeSource {
    fun getUserInfo() : Single<UserData>
    fun cleanUserInfo()
}