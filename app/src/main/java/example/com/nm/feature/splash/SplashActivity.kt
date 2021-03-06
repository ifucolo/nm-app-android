package example.com.nm.feature.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import example.com.nm.R
import example.com.nm.feature.home.ui.HomeActivity
import example.com.nm.feature.login.ui.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModel()
        Handler().postDelayed({
            viewModel.checkToken()
        }, 700)
    }

    private fun setupViewModel() {
        viewModel.hasToken.observe(this, Observer { hasToken ->
            when(hasToken) {
                true -> showHomeActivity()
                false -> showLoginActivity()
            }
        })
    }

    private fun showHomeActivity() {
        startActivity(HomeActivity.launchIntent(this))
        finish()
    }

    private fun showLoginActivity() {
        startActivity(LoginActivity.launchIntent(this))
        finish()
    }

}
