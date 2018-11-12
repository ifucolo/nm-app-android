package example.com.nm.feature.di

import example.com.nm.feature.login.domain.LoginSource
import example.com.nm.feature.login.repository.LoginRepository
import example.com.nm.feature.login.ui.LoginViewModel
import example.com.nm.feature.pref.NmPref
import example.com.nm.feature.splash.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.mockito.Mockito.mock


val splashViewModelTest = module {
    viewModel { SplashViewModel(mock(NmPref::class.java)) }
}

val loginViewModelTest = module {
    viewModel { LoginViewModel(mock(LoginSource::class.java)) }
}

val testModule = listOf(splashViewModelTest)