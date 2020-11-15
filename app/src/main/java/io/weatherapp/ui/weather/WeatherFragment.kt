package io.weatherapp.ui.weather

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import io.weatherapp.R
import io.weatherapp.ui.BaseFragment
import kotlinx.android.synthetic.main.weather_fragment.*

class WeatherFragment : BaseFragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        pb.isVisible = true
        viewModel.errorMsg observe {
            pb.isVisible = false
            showDialog(requireContext(), null, it)
        }

        viewModel.getWeatherLocation()
        viewModel.weatherModel.observe { weather ->
            pb.isVisible = false

            val temp = weather.current?.temp.toString() + "°"
//            val temp = weather.weatherList?.get(0)?.main?.temp_max.toString() + "°/" +weather.weatherList?.get(0)?.main?.temp_min.toString() + "°"
            tvTemp.text = temp
            tvHumidity.text = weather.current?.humidity?.toString()

            ivMainWeather.setImageDrawable(
                getWeatherIcon(weather.current?.weather?.get(0)?.main ?: "")
            )
        }
    }

    fun getWeatherIcon(weather: String): Drawable {
        when (weather) {
            "Thunderstorm" -> return resources.getDrawable(R.drawable.ic_white_day_thunder)
            "Drizzle" -> return resources.getDrawable(R.drawable.ic_white_day_shower)
            "Rain" -> return resources.getDrawable(R.drawable.ic_white_day_shower)
            "Snow" -> return resources.getDrawable(R.drawable.ic_location_on_24)
            "Clear" -> return resources.getDrawable(R.drawable.ic_white_day_bright)
            "Clouds" -> return resources.getDrawable(R.drawable.ic_white_day_cloudy)
        }
        return resources.getDrawable(R.drawable.ic_location_on_24)
    }

}