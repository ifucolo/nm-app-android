package example.com.nm.feature.home.repository.model

import com.google.gson.annotations.SerializedName


class UserDataPayload(
    @SerializedName("email") val email: String,
    @SerializedName("country") val country: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("status") val status: String
)