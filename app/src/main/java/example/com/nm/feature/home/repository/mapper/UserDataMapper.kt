package example.com.nm.feature.home.repository.mapper

import example.com.nm.feature.home.domain.entity.UserData
import example.com.nm.feature.home.repository.model.UserDataPayload

object UserDataMapper {

    fun map(payload: UserDataPayload) = UserData(
        email = payload.email,
        country = payload.country,
        lastName = payload.lastName,
        firstName = payload.firstName,
        status = payload.status
    )
}