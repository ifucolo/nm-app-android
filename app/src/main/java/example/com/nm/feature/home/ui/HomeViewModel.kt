package example.com.nm.feature.home.ui

import android.arch.lifecycle.MutableLiveData
import example.com.catdogapp.utill.RxUtils
import example.com.nm.feature.home.domain.HomeSource
import example.com.nm.feature.home.domain.entity.UserData
import example.com.nm.util.rx.ReactiveViewModel
import io.reactivex.rxkotlin.plusAssign

class HomeViewModel(private val source: HomeSource): ReactiveViewModel() {

    val uiData = MutableLiveData<ResultUIModel>()

    fun getUserInfo() {
        disposables += source.getUserInfo()
            .compose(RxUtils.applySingleSchedulers())
            .doOnSubscribe { loadData.value = true }
            .doFinally { loadData.value = false }
            .subscribe(
                {
                    uiData.value = ResultUIModel(userData = it)
                },
                {
                    uiData.value = ResultUIModel(error = it)
                }
            )
    }

    fun cleanUserData() {
        source.cleanUserInfo()
    }

    data class ResultUIModel(val userData: UserData? = null, val error: Throwable? = null)

}