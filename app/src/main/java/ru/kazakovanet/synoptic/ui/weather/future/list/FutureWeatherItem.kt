package ru.kazakovanet.synoptic.ui.weather.future.list

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry
import ru.kazakovanet.synoptic.internal.glide.GlideApp

/**
 * Created by NKazakova on 08.07.2020.
 */
class FutureWeatherItem(
    val weatherEntry: FutureWeatherEntry
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            // TODO: 08.07.2020
            textView_condition.text = "condition"
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_future_weather

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        // TODO: 08.07.2020  
        textView_date.text = weatherEntry.getLocalDate("America/Chicago").format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        // TODO: 08.07.2020
        val unitAbbreviation = "°C"
        textView_temperature.text =
            "${weatherEntry.temp.day.toInt()}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load(weatherEntry.conditionIconUrl)
            .into(imageView_condition_icon)
    }
}