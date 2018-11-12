package example.com.nm.feature.di

import example.com.nm.feature.home.domain.HomeSource
import example.com.nm.feature.home.repository.HomeRepository
import example.com.nm.feature.home.ui.HomeViewModel
import example.com.nm.feature.login.domain.LoginSource
import example.com.nm.feature.login.repository.LoginRepository
import example.com.nm.feature.login.ui.LoginViewModel
import example.com.nm.feature.pref.NmPref
import example.com.nm.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val appModule = module {
    single<LoginSource> { LoginRepository(get(), get()) }
    single<HomeSource> { HomeRepository(get(), get()) }

    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

val prefModule = module {
    single  { NmPref(androidContext()) }
}