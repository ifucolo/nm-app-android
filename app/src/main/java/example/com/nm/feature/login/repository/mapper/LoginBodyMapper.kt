package example.com.nm.feature.login.repository.mapper

import com.google.gson.JsonObject

object LoginBodyMapper {

    fun map(username: String, password: String) = JsonObject().apply {
        addProperty("username", username)
        addProperty("password", password)
        addProperty("grant_type", "password")
    }
}