package ru.kazakovanet.synoptic.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.network.api.BASE_WEATHER_IMAGE_URL
import ru.kazakovanet.synoptic.internal.glide.GlideApp
import ru.kazakovanet.synoptic.ui.base.ScopedFragment

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUi()
    }

    private fun bindUi() = launch {
        if (!viewModel.isAccessTokenReceived()) {
            showAuthScreen(group_loading)
            return@launch
        }

        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.city)
        })
        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE

            updateDateToToday()

            updateTemperature(it.temperature)
            updateCondition(it.conditionText)
            updateHumidity(it.humidity)
            updateWind(it.windSpeed)
            updateVisibility(it.visibility)
            updatePressure(it.pressure, it.rising)
            updateSunriseSunset(it.sunrise, it.sunset)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("$BASE_WEATHER_IMAGE_URL${it.conditionCode}.gif")
                .into(imageView_condition_icon)
        })
    }

    private fun showAuthScreen(view: View) {
        val actionAuth = CurrentWeatherFragmentDirections.actionAuth()
        Navigation.findNavController(view).navigate(actionAuth)
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperature(temperature: Int) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updateHumidity(humidityPercent: Int) {
        textView_humidity.text = "Humidity: $humidityPercent%"
    }

    private fun updateWind(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun updatePressure(pressure: Double, rising: Int) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mbar", "ih")
        textView_pressure.text = "Pressure: $pressure $unitAbbreviation"
        val resourceId =
            resources.getIdentifier("drawable/pressure_icon_$rising", null, context?.packageName)

        imageView_pressure_icon.setImageDrawable(context?.getDrawable(resourceId))
    }

    private fun updateSunriseSunset(sunrise: String, sunset: String) {
        textView_sunrise.text = "Sunrise: $sunrise"
        textView_sunset.text = "Sunset: $sunset"
    }
}