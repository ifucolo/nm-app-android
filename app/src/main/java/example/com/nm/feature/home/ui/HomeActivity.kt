package example.com.nm.feature.home.ui

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import example.com.nm.R
import example.com.nm.feature.di.BaseActivity
import example.com.nm.feature.home.domain.entity.UserData
import example.com.nm.feature.home.repository.ExpiredTokenException
import example.com.nm.feature.login.ui.LoginActivity
import example.com.nm.util.Util
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_menu.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity(), OnMapReadyCallback , NavigationView.OnNavigationItemSelectedListener{

    private val viewModel: HomeViewModel by viewModel()

    companion object {

        fun launchIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupMap()
        bindListener()
        setupViewModel()
        setData()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
    }

    private fun bindListener() {
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }
    }

    private fun setupViewModel() {
        viewModel.loadData.observe(this, Observer { isLoading ->
            when(isLoading) {
                true -> showLoading()
                false -> hideLoading()
            }
        })

        viewModel.uiData.observe(this, Observer { uiData ->
           when{
               uiData?.userData != null -> setUserData(uiData.userData)
               uiData?.error is ExpiredTokenException -> logout()
               else -> setNoConnectionError()
           }
        })
    }

    private fun setData() {
        viewModel.getUserInfo()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun setUserData(userData: UserData) {
        txtStatus.text = userData.status
        txtUserName.text = userData.toString()
    }

    private fun logout() {
        viewModel.cleanUserData()
        startActivity(LoginActivity.launchIntent(this))
    }

    private fun setNoConnectionError() {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val chargePoints = Util.getChargePoints(this)

        chargePoints.forEach {
            val chargePoint = LatLng(it.lat, it.lng)

            googleMap?.addMarker(
                MarkerOptions().position(chargePoint)
                    .title(it.city)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
            )
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_official -> Util.openUri(this, getString(R.string.play_url))
            R.id.nav_logout -> logout()
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}
