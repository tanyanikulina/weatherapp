package io.weatherapp.ui.weather

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import io.weatherapp.R
import io.weatherapp.ui.base.BaseFragment
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

        viewModel.currentWeather.observe {
            pb.isVisible = false
//            val temp = weather.current?.temp.toString() + "°/" + weather.current?.feelsLike.toString() + "°"
            tvTemp.text = it.temp.toString() + "°"
            tvHumidity.text = it.humidity?.toString() + "%"
            tvWind.text = it.windSpeed.toString() + "м/сек"

            ivWind.setImageDrawable(getWindIcon(it.windDegrees ?: 0))
            ivMainWeather.setImageDrawable(getWeatherIcon(it.weather?.get(0)?.main ?: ""))
        }

    }

    fun getWeatherIcon(weather: String): Drawable {
        when (weather) {
            "Thunderstorm" -> return resources.getDrawable(R.drawable.ic_white_day_thunder)
            "Drizzle" -> return resources.getDrawable(R.drawable.ic_white_day_shower)
            "Rain" -> return resources.getDrawable(R.drawable.ic_white_day_shower)
            "Snow" -> return resources.getDrawable(R.drawable.ic_white_day_shower)
            "Mist" -> return resources.getDrawable(R.drawable.ic_white_day_cloudy)
            "Clear" -> return resources.getDrawable(R.drawable.ic_white_day_bright)
            "Clouds" -> return resources.getDrawable(R.drawable.ic_white_day_cloudy)
        }
        return resources.getDrawable(R.drawable.ic_location_on_24)
    }

    fun getWindIcon(wind: Int): Drawable {
        if (wind > 339 || wind <= 23) return resources.getDrawable(R.drawable.ic_icon_wind_n)
        if (wind in 24..68) return resources.getDrawable(R.drawable.ic_icon_wind_ne)
        if (wind in 69..113) return resources.getDrawable(R.drawable.ic_icon_wind_e)
        if (wind in 114..158) return resources.getDrawable(R.drawable.ic_icon_wind_se)
        if (wind in 159..203) return resources.getDrawable(R.drawable.ic_icon_wind_s)
        if (wind in 204..248) return resources.getDrawable(R.drawable.ic_icon_wind_ws)
        if (wind in 249..293) return resources.getDrawable(R.drawable.ic_icon_wind_w)
        if (wind in 294..338) return resources.getDrawable(R.drawable.ic_icon_wind_wn)
        return resources.getDrawable(R.drawable.ic_location_on_24)
    }

}