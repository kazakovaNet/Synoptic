package ru.kazakovanet.synoptic.ui.weather.forecast

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_forecast_weather.*
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity
import ru.kazakovanet.synoptic.data.network.api.BASE_WEATHER_IMAGE_URL
import ru.kazakovanet.synoptic.internal.glide.GlideApp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by NKazakova on 08.07.2020.
 */
class ForecastWeatherItem(
    val forecastWeatherEntity: ForecastWeatherEntity
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = forecastWeatherEntity.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_forecast_weather

    private fun ViewHolder.updateDate() {
        val dtFormatter = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        textView_date.text =
            "${forecastWeatherEntity.day}, ${dtFormatter.format(Date(forecastWeatherEntity.date))}"
    }

    private fun ViewHolder.updateTemperature() {
        // TODO: 08.07.2020
        val unitAbbreviation = "°C"
        textView_temperature.text =
            "${forecastWeatherEntity.low}...${forecastWeatherEntity.high}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load("$BASE_WEATHER_IMAGE_URL${forecastWeatherEntity.conditionCode}.gif")
            .into(imageView_condition_icon)
    }
}