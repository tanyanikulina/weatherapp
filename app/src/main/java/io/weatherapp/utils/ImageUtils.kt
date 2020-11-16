package io.weatherapp.utils

import io.weatherapp.R

object ImageUtils {

    fun getWeatherRes(weather: String): Int {
        when (weather) {
            "Thunderstorm" -> return R.drawable.ic_white_day_thunder
            "Rain" -> return R.drawable.ic_white_day_rain
            "Drizzle" -> return R.drawable.ic_white_day_shower
            "Snow" -> return R.drawable.ic_white_day_shower
            "Mist" -> return R.drawable.ic_white_day_cloudy
            "Clear" -> return R.drawable.ic_white_day_bright
            "Clouds" -> return R.drawable.ic_white_day_cloudy
        }
        return R.drawable.ic_white_day_cloudy
    }

    fun getWindRes(wind: Int): Int {
        if (wind > 339 || wind <= 23) return R.drawable.ic_icon_wind_n
        if (wind in 24..68) return R.drawable.ic_icon_wind_ne
        if (wind in 69..113) return R.drawable.ic_icon_wind_e
        if (wind in 114..158) return R.drawable.ic_icon_wind_se
        if (wind in 159..203) return R.drawable.ic_icon_wind_s
        if (wind in 204..248) return R.drawable.ic_icon_wind_ws
        if (wind in 249..293) return R.drawable.ic_icon_wind_w
        if (wind in 294..338) return R.drawable.ic_icon_wind_wn
        return R.drawable.ic_icon_wind_n
    }

}