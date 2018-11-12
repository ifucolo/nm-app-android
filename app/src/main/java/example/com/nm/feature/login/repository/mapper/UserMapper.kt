package example.com.nm.feature.login.repository.mapper

import example.com.nm.feature.login.domain.entity.User
import example.com.nm.feature.login.repository.model.UserPayload

object UserMapper {

    fun map(payload: UserPayload) = User(
        tokenType = payload.tokenType,
        accessToken = payload.accessToken,
        expiresIn = payload.expiresIn,
        refreshToken = payload.refreshToken
    )
}