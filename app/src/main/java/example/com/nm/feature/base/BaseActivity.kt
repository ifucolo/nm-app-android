package example.com.nm.feature.base

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import example.com.nm.R
import example.com.nm.util.extensions.progressDialog

open class BaseActivity: AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null


    protected fun hideLoading() {
        if (progressDialog?.isShowing == true)
            progressDialog!!.dismiss()
    }

    protected fun showLoading() {
        progressDialog = progressDialog(R.string.loading)
    }
}