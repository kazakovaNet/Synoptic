package ru.kazakovanet.synoptic.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.network.api.AUTH_URL
import ru.kazakovanet.synoptic.data.network.api.CLIENT_ID
import ru.kazakovanet.synoptic.data.network.api.GRANT_TYPE
import ru.kazakovanet.synoptic.data.network.api.REDIRECT_URI
import ru.kazakovanet.synoptic.data.network.api.yahoo.YahooWeatherApiService

const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback() {
    }
    private lateinit var navController: NavController
    private val api: YahooWeatherApiService by instance()
    private var switcher = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                AUTH_URL +
                        "request_auth" +
                        "?client_id=$CLIENT_ID" +
                        "&redirect_uri=${REDIRECT_URI}" +
                        "&response_type=code" +
                        "&language=en-us"
            )
        )

        startActivity(intent)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottom_nav.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)

        if (hasLocationPermission()) bindLocationManager()
        else requestLocationPermission()
    }

    override fun onResume() {
        super.onResume()

        if (switcher) return
        getAuthToken()
        switcher = !switcher
    }

    private fun getAuthToken() {
        val uri = intent.data ?: return
        if (!uri.toString().startsWith(REDIRECT_URI)) return

        val code = uri.getQueryParameter("code") ?: return

        GlobalScope.launch {
            val fieldMap = mapOf(
                Pair("redirect_uri", REDIRECT_URI),
                Pair("grant_type", GRANT_TYPE),
                Pair("code", code)
            )
            val accessToken = api.getAccessToken(fieldMap).await()
            println(accessToken.accessToken)
        }
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