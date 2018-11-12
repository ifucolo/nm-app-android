package example.com.nm.feature.login.repository.model

import com.google.gson.annotations.SerializedName

class UserPayload(
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: String,
    @SerializedName("refresh_token") val refreshToken: String
)