package io.weatherapp.models

import io.weatherapp.data.DailyWeather

data class DayModel(val dayWeather: DailyWeather, var isSelected: Boolean) {

}