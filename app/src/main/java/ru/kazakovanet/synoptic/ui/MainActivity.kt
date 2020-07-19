package ru.kazakovanet.synoptic.ui

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.network.api.AUTH_URL
import ru.kazakovanet.synoptic.data.network.api.CLIENT_ID
import ru.kazakovanet.synoptic.data.network.api.REDIRECT_URI
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepository

const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback() {
    }
    private lateinit var navController: NavController
    private val repository: YahooAuthApiRepository by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        GlobalScope.launch(Dispatchers.Main) {
            if (repository.isAccessTokenReceived()) {
                showCurrentWeather()
                return@launch
            }

            webVIew.webViewClient = object : WebViewClient() {
                @TargetApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    if (!request.url.toString().startsWith(REDIRECT_URI)) {
                        view.loadUrl(request.url.toString())
                    } else {
                        getAuthToken(request.url)
                    }
                    return true
                }
            }

            webVIew.loadUrl(
                AUTH_URL +
                        "request_auth" +
                        "?client_id=$CLIENT_ID" +
                        "&redirect_uri=${REDIRECT_URI}" +
                        "&response_type=code" +
                        "&language=en-us"
            )
        }
    }

    private fun getAuthToken(uri: Uri) {
        val code = uri.getQueryParameter("code") ?: return

        GlobalScope.launch(Dispatchers.Main) {
            repository.getAccessToken(code).observe(this@MainActivity, Observer {
                println("Getting token: ${it.accessToken}")

                showCurrentWeather()
            })
        }
    }

    private fun showCurrentWeather() {
        webVIew.visibility = View.GONE

        navController =
            Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this@MainActivity, navController)

        if (hasLocationPermission()) bindLocationManager()
        else requestLocationPermission()
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient,
            locationCallback
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocationManager()
            else
                Toast.makeText(
                    this,
                    "Please, set location manually in settings",
                    Toast.LENGTH_SHORT
                ).show()
        }

    }
}