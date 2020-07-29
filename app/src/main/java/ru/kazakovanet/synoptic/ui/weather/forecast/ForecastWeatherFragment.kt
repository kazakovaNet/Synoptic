package ru.kazakovanet.synoptic.ui.weather.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.current_weather_fragment.group_loading
import kotlinx.android.synthetic.main.forecast_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity
import ru.kazakovanet.synoptic.ui.base.ScopedFragment

class ForecastWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: ForecastWeatherViewModelFactory by instance()

    private lateinit var viewModel: ForecastWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forecast_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ForecastWeatherViewModel::class.java)

        bindUi()
    }

    private fun bindUi() = launch {
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location ->
            if (location == null) return@Observer

            updateLocation(location.city)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer { weatherEntries ->
            if (weatherEntries == null) return@Observer

            group_loading.visibility = View.GONE

            updateDateToNextWeek()
            initRecyclerView(weatherEntries.toFutureWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

    private fun List<ForecastWeatherEntity>.toFutureWeatherItems(): List<ForecastWeatherItem> {
        return map {
            ForecastWeatherItem(
                it
            )
        }
    }

    private fun initRecyclerView(items: List<ForecastWeatherItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ForecastWeatherFragment.context)
            adapter = groupAdapter
        }
    }
}