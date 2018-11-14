package example.com.nm.feature.home.domain.entity

class UserData(
    val email: String,
    val country: String,
    val lastName: String,
    val firstName: String,
    val status: String
) {
    override fun toString(): String {
        return firstName + lastName
    }
}