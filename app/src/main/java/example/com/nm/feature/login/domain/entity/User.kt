package example.com.nm.feature.login.domain.entity

class User(
    val tokenType: String,
    val accessToken: String,
    val expiresIn: String,
    val refreshToken: String
)