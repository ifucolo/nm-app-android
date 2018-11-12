package example.com.nm.feature.api

import example.com.nm.feature.home.repository.model.UserDataPayload
import example.com.nm.feature.login.repository.model.UserPayload
import io.reactivex.Single
import retrofit2.http.*

interface ServerApi {

    @FormUrlEncoded
    @POST("/oauth2/access_token")
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("grant_type") grant_type: String): Single<UserPayload>


    @GET("/v1/me")
    fun getUserInfo(): Single<UserDataPayload>
}
