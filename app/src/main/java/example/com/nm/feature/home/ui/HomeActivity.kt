package example.com.nm.feature.home.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import example.com.nm.R
import example.com.nm.feature.base.BaseActivity
import example.com.nm.feature.home.domain.entity.UserData
import example.com.nm.feature.home.repository.ExpiredTokenException
import example.com.nm.feature.login.ui.LoginActivity
import example.com.nm.util.Util
import example.com.nm.util.extensions.action
import example.com.nm.util.extensions.snack
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_menu.*
import org.koin.android.viewmodel.ext.android.viewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider

@RuntimePermissions
class HomeActivity : BaseActivity(), OnMapReadyCallback , NavigationView.OnNavigationItemSelectedListener{

    private val viewModel: HomeViewModel by viewModel()
    private var googleMap: GoogleMap? = null

    companion object {

        const val RESULT_SETTINGS_LOCATION = 983

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
        requestGeoLocationWithPermissionCheck()
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
            }
        })
    }

    private fun setData() {
        viewModel.getUserInfo()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun setUserData(userData: UserData) {
        txtStatus?.text = userData.status
        txtUserName?.text = userData.toString()
    }

    private fun logout() {
        viewModel.cleanUserData()
        startActivity(LoginActivity.launchIntent(this))
        finish()
    }

    private fun setupMapView() {
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

    @SuppressLint("MissingPermission")
    @NeedsPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun requestGeoLocation() {
        val disposable = ReactiveLocationProvider(this@HomeActivity)
            .lastKnownLocation
            .subscribe { lastLocation ->
                val myLocation =  LatLng(lastLocation.latitude, lastLocation.longitude)
                googleMap?.isMyLocationEnabled = true
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                googleMap?.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 2000, null)
            }

        addDisposable(disposable)
    }

    @OnPermissionDenied(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    internal fun permissionDenied() {
        openSnackAction()
    }

    @OnNeverAskAgain(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    internal fun permissionNeverAskAgain() {
        openSnackAction()
    }

    private fun openSnackAction() {
        drawerLayout.snack(R.string.settings_location, Snackbar.LENGTH_INDEFINITE) {
            action(R.string.settings, ContextCompat.getColor(this@HomeActivity, R.color.colorPrimary)) { actionView ->
                openAppSettings()
                dismiss()
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =  Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, RESULT_SETTINGS_LOCATION)
    }

    private fun checkRequestPermission() {
        if (googleMap != null)
            requestGeoLocationWithPermissionCheck()
    }

    override fun onMapReady(googleMapReady: GoogleMap?) {
        googleMap = googleMapReady
        setupMapView()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_official -> Util.openUri(this, getString(R.string.play_url))
            R.id.nav_logout -> logout()
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onStart() {
        checkRequestPermission()

        super.onStart()
    }

}
