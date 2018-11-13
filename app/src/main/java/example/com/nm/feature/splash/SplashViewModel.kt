package example.com.nm.feature.splash

import android.arch.lifecycle.MutableLiveData
import example.com.nm.feature.pref.Consts
import example.com.nm.feature.pref.NmPref
import example.com.nm.util.rx.ReactiveViewModel

class SplashViewModel(private val nmPref: NmPref): ReactiveViewModel() {

    val hasToken = MutableLiveData<Boolean>()

    fun checkToken() {
        hasToken.value = !nmPref.getString(Consts.ACCESS_TOKEN).isNullOrEmpty()
    }

    fun chec() = true

}