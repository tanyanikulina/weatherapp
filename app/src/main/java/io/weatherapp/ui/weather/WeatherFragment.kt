package io.weatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.weatherapp.R
import io.weatherapp.data.CurrentWeather
import io.weatherapp.ui.base.BaseFragment
import io.weatherapp.utils.DateUtils
import io.weatherapp.utils.DateUtils.DF_WEEK_DAY_MONTH
import io.weatherapp.utils.ImageUtils
import kotlinx.android.synthetic.main.weather_fragment.*

class WeatherFragment : BaseFragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel
    private var dayAdapter: DayAdapter = DayAdapter()
    private var hourAdapter: HourAdapter = HourAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        rvDaily.adapter = dayAdapter
        rvHourly.adapter = hourAdapter
        pb.isVisible = true

        viewModel.currentWeather.observe {
            pb.isVisible = false
            fillMainWeather(it)
        }
        viewModel.dailyWeather.observe {
            dayAdapter.setNewData(it)
        }
        viewModel.hourlyWeather.observe {
            hourAdapter.setNewData(it)
        }
        viewModel.errorMsg observe {
            pb.isVisible = false
            showDialog(requireContext(), null, it)
        }

        ivLocation.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_mapsFragment)
        }
        checkBackStack()
    }

    private fun fillMainWeather(weather: CurrentWeather) {

        tvDate.text =
            DateUtils.formatDateFromMillis((weather.date ?: 0L) * 1000L, DF_WEEK_DAY_MONTH)

        tvTemp.text = weather.temp.toString() + "°"
        tvHumidity.text = weather.humidity?.toString() + "%"
        tvWind.text = weather.windSpeed.toString() + " м/сек"

        ivWind.setImageDrawable(
            resources.getDrawable(ImageUtils.getWindRes(weather.windDegrees ?: 0))
        )
        ivMainWeather.setImageDrawable(
            resources.getDrawable(ImageUtils.getWeatherRes(weather.weather?.get(0)?.main ?: ""))
        )
    }

    private fun checkBackStack() {
        var lat: Double
        var lon: Double
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Pair<Double, Double>>(
            "lat_lon"
        )
            ?.observe {
                lat = it.first
                lon = it.second
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Double>("lat")

                pb.isVisible = true
                viewModel.getWeatherLocation(lat, lon)
            }
    }

}