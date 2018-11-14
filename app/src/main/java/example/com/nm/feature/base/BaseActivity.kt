package example.com.nm.feature.base

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import example.com.nm.R
import example.com.nm.util.extensions.progressDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseActivity: AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    private var disposables: CompositeDisposable? = null

    protected fun addDisposable(disposable: Disposable) {
        if (disposables == null)
            disposables = CompositeDisposable()

        disposables?.add(disposable)
    }

    protected fun hideLoading() {
        if (progressDialog?.isShowing == true)
            progressDialog!!.dismiss()
    }

    protected fun showLoading() {
        progressDialog = progressDialog(R.string.loading)
    }

    override fun onDestroy() {
        if (disposables != null)
            disposables?.clear()

        super.onDestroy()
    }
}