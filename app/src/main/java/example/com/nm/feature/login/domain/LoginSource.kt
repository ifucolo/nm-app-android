package example.com.nm.feature.login.domain

import io.reactivex.Completable

interface LoginSource {

    fun login(username: String, password: String): Completable
}