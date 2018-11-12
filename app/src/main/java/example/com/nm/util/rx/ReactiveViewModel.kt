package example.com.nm.util.rx

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class ReactiveViewModel: ViewModel() {

    val loadData = MutableLiveData<Boolean>()

    val disposables = CompositeDisposable()

    fun disposables() = disposables

    fun unbind() {
        this.disposables.clear()
    }
}
