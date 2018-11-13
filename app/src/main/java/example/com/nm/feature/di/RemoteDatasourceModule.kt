package example.com.nm.feature.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import example.com.nm.BuildConfig
import example.com.nm.R
import example.com.nm.feature.api.ServerApi
import example.com.nm.feature.pref.Consts
import example.com.nm.feature.pref.NmPref
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val remoteDatasourceModule = module {
    single { providesGson() }
    single { providesInterceptorAuth(get(), androidContext().getString(R.string.basic_header)) }
    single { providesOkHttpClient(get()) }

    single { createWebService<ServerApi>(get(), get(), androidContext().getString(R.string.base_url)) }
}

fun providesGson(): Gson {
    val gsonBuilder = GsonBuilder()
    return gsonBuilder.create()
}


fun providesInterceptorAuth(nmPref: NmPref, basicHeader: String) = Interceptor { chain ->
    val request = chain.request()
    val builder = request.newBuilder()

    val token = nmPref.getString(Consts.ACCESS_TOKEN, null)

    if (request.header("No-Authentication") == null) {
        if (token != null)
            builder.header("Authorization", token)
        else
            builder.header("Authorization", basicHeader)
    }

    chain.proceed(builder.build())
}

fun providesOkHttpClient(interceptorAuth: Interceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG)
        builder.addNetworkInterceptor(StethoInterceptor())

    builder.addInterceptor(interceptorAuth)
    builder.connectTimeout(30, TimeUnit.SECONDS)
    builder.readTimeout(30, TimeUnit.SECONDS)
    builder.writeTimeout(30, TimeUnit.SECONDS)

    return builder.build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, gson: Gson, url: String): T {
    return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .client(okHttpClient)
            .build()
            .create(T::class.java)
}