package example.com.nm.feature.login.ui

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import example.com.nm.R
import example.com.nm.feature.di.BaseActivity
import example.com.nm.feature.home.ui.HomeActivity
import example.com.nm.util.Util.closeInput
import example.com.nm.util.extensions.onActionDone
import example.com.nm.util.extensions.snack
import example.com.nm.util.extensions.textWatcher
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_input_edit_text.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModel()


    companion object {

        fun launchIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViewModel()
        bindListener()
    }

    override fun onDestroy() {
        viewModel.unbind()

        super.onDestroy()
    }

    private fun setupViewModel() {
        viewModel.uiData.observe(this, Observer { uiData ->
            when{
                uiData?.loginData == true -> showLoginSuccess()
                !uiData?.error.isNullOrEmpty() -> showError(uiData?.error!!)
                else -> { showUnexpectedError() }
            }
        })

        viewModel.loadData.observe(this, Observer { isLoading ->
            when(isLoading) {
                true -> showLoading()
                false -> hideLoading()
            }
        })

        viewModel.buttonData.observe(this, Observer { isEnable ->
            btnLogin.isEnabled = isEnable!!
        })

        viewModel.emailData.observe(this, Observer { emailIsValid ->
            if (emailIsValid == false)
                btnLogin.snack(R.string.invalid_email) {}
        })
    }

    private fun bindListener() {
        btnLogin.setOnClickListener {
            closeInput(inputEmail, false)
            viewModel.loginValidate(
                username = inputEmail.editText.text.toString(),
                password = inputPassword.editText.text.toString()
            )
        }

        inputEmail.editText.textWatcher {
            afterTextChanged {
                viewModel.checkFieldNotEmpty(username = it, password = inputPassword.editText.text.toString().trim())
            }
        }

        inputPassword.editText.textWatcher {
            afterTextChanged {
                viewModel.checkFieldNotEmpty(username = inputEmail.editText.text.toString().trim(), password = it)
            }
        }

        inputPassword.editText.onActionDone {
            btnLogin.callOnClick()
        }

    }

    private fun showLoginSuccess() {
        startActivity(HomeActivity.launchIntent(this))
    }

    private fun showError(errorMsg: String) {
        layoutMain.snack(errorMsg) {}
    }

    private fun showUnexpectedError() {
        layoutMain.snack(R.string.username_password_wrong) {}
    }
}
