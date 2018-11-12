package example.com.nm.feature.login.ui

import android.arch.lifecycle.MutableLiveData
import android.support.v4.util.PatternsCompat.EMAIL_ADDRESS
import com.google.gson.JsonParser
import example.com.catdogapp.utill.RxUtils
import example.com.nm.feature.login.domain.LoginSource
import example.com.nm.util.rx.ReactiveViewModel
import io.reactivex.rxkotlin.plusAssign
import retrofit2.HttpException

class LoginViewModel(val source: LoginSource) : ReactiveViewModel() {

    val uiData = MutableLiveData<ResultUIModel>()
    val buttonData = MutableLiveData<Boolean>()
    val emailData = MutableLiveData<Boolean>()

    fun checkFieldNotEmpty(username: String, password: String) {
        buttonData.value = username.isNotEmpty() && password.isNotEmpty()
    }

    fun loginValidate(username: String, password: String) {
        val isValidEmail = EMAIL_ADDRESS.matcher(username).matches()

        if (isValidEmail) {
            emailData.value = true
            login(username, password)
        } else
            emailData.value = false
    }

    fun login(username: String, password: String) {
        disposables += source.login(username, password)
            .compose(RxUtils.applyCompletableSchedulers())
            .doOnSubscribe { loadData.value = true }
            .doFinally { loadData.value = false }
            .subscribe(
                {
                    uiData.value = ResultUIModel(true)
                }
                ,{
                    uiData.value = ResultUIModel(error = checkError(it))
                }
            )
    }


    private fun checkError(throwable: Throwable) : String? {
        return if (throwable is HttpException && throwable.code() == 401) {
            val responseString = throwable.response().errorBody()?.string()
            val responseJson = JsonParser().parse(responseString).asJsonObject

            return responseJson.get("error_description").asString
        } else
            null
    }

    data class ResultUIModel(val loginData: Boolean = false, val error: String? = null)

}