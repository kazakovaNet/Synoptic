package ru.kazakovanet.synoptic.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import ru.kazakovanet.synoptic.data.db.entity.LocationEntity
import ru.kazakovanet.synoptic.internal.LocationPermissionNotGrantedException
import ru.kazakovanet.synoptic.internal.asDeferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context),
    LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(location: LocationEntity): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(location)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(location as? LocationEntity)
    }

    override suspend fun getPreferredLocationString(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (isUsingDeviceLocation()) {
            return try {
                when (val deviceLocation = getLastDeviceLocation().await()) {
                    null -> {
                        map["location"] = "${getCustomLocationName()}"
                    }
                    else -> {
                        map["lat"] = "${deviceLocation.latitude}"
                        map["lon"] = "${deviceLocation.longitude}"
                    }
                }
                map
            } catch (e: LocationPermissionNotGrantedException) {
                map["location"] = "${getCustomLocationName()}"
                map
            }
        } else {
            map["location"] = "${getCustomLocationName()}"
            return map
        }
    }

    private suspend fun hasDeviceLocationChanged(location: LocationEntity): Boolean {
        if (!isUsingDeviceLocation()) {
            return false
        }

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - location.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - location.lon) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(location: LocationEntity?): Boolean {
        if (location == null) return false

        if (!isUsingDeviceLocation()) {
            val customLocation = getCustomLocationName()
            return customLocation != location.city
        }
        return false
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermissions()) fusedLocationProviderClient.lastLocation.asDeferred()
        else throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}