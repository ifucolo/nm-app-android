package example.com.nm.feature.commom

import example.com.nm.feature.pref.NmPref
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val commomModule = module {
    single  { NmPref(androidContext()) }
}