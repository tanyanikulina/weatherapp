package io.weatherapp.api.responses

import com.squareup.moshi.Json

data class ResponseWeatherModel(
    @field:Json(name = "lat") val lat: Float?
    , @field:Json(name = "lon") val lon: Float?
    , @field:Json(name = "timezone") val timezone: String?
    , @field:Json(name = "timezone_offset") val timezoneOffset: Long?
    , @field:Json(name = "current") val current: CurrentModel?
    , @field:Json(name = "hourly") val hourly: List<HourlyModel>?
    , @field:Json(name = "daily") val daily: List<DailyModel>?
)

data class CurrentModel(
    @field:Json(name = "dt") val date: Long?
    , @field:Json(name = "sunrise") val sunrise: Long?
    , @field:Json(name = "sunset") val sunset: Long?
    , @field:Json(name = "temp") val temp: Float?
    , @field:Json(name = "feels_like") val feelsLike: Float?
    , @field:Json(name = "pressure") val pressure: Int?
    , @field:Json(name = "humidity") val humidity: Int?
    , @field:Json(name = "dew_point") val dewPoint: Float?
    , @field:Json(name = "uvi") val uvi: Float?  //Midday UV index
    , @field:Json(name = "clouds") val clouds: Int?
    , @field:Json(name = "visibility") val visibility: Long? // Average visibility, metres
    , @field:Json(name = "wind_speed") val windSpeed: Float?
    , @field:Json(name = "wind_deg") val windDegrees: Int?
    , @field:Json(name = "weather") val weather: List<WeatherModel>?
    , @field:Json(name = "rain") val rain: SnowRainModel?
    , @field:Json(name = "snow") val snow: SnowRainModel?
)

data class HourlyModel(
    @field:Json(name = "dt") val date: Long?
    , @field:Json(name = "temp") val temp: Float?
    , @field:Json(name = "feels_like") val feelsLike: Float?
    , @field:Json(name = "pressure") val pressure: Int?
    , @field:Json(name = "humidity") val humidity: Int?
    , @field:Json(name = "dew_point") val dewPoint: Float?
    , @field:Json(name = "clouds") val clouds: Int?
    , @field:Json(name = "visibility") val visibility: Long? // Average visibility, metres
    , @field:Json(name = "wind_speed") val windSpeed: Float?
    , @field:Json(name = "wind_deg") val windDegrees: Int?
    , @field:Json(name = "wind_gust") val windGust: Float?
    , @field:Json(name = "pop") val pop: Float?
    , @field:Json(name = "weather") val weather: List<WeatherModel>?
    , @field:Json(name = "rain") val rain: SnowRainModel?
    , @field:Json(name = "snow") val snow: SnowRainModel?
)

data class DailyModel(
    @field:Json(name = "dt") val date: Long?
    , @field:Json(name = "sunrise") val sunrise: Long?
    , @field:Json(name = "sunset") val sunset: Long?
    , @field:Json(name = "temp") val temp: TemperatureModel?
    , @field:Json(name = "feels_like") val feelsLike: TemperatureModel?
    , @field:Json(name = "pressure") val pressure: Int?
    , @field:Json(name = "humidity") val humidity: Int?
    , @field:Json(name = "dew_point") val dewPoint: Float?
    , @field:Json(name = "wind_speed") val windSpeed: Float?
    , @field:Json(name = "wind_gust") val windGust: Float?
    , @field:Json(name = "wind_deg") val windDegrees: Int?
    , @field:Json(name = "clouds") val clouds: Int?
    , @field:Json(name = "uvi") val uvi: Float?  //Midday UV index
    , @field:Json(name = "visibility") val visibility: Long? // Average visibility, metres
    , @field:Json(name = "pop") val pop: Float?
    , @field:Json(name = "rain") val rain: Float?
    , @field:Json(name = "snow") val snow: Float?
    , @field:Json(name = "weather") val weather: List<WeatherModel>?
//    , @field:Json(name = "alerts") val alerts: List<AlertsModel>?
)

data class WeatherModel(
    @field:Json(name = "id") val id: Int?
    , @field:Json(name = "main") val main: String?
    , @field:Json(name = "description") val description: String?
    , @field:Json(name = "icon") val icon: String?
)

data class SnowRainModel(
    @field:Json(name = "1h") val h1: Float?
    , @field:Json(name = "3h") val h3: Float?
)

data class TemperatureModel(
    @field:Json(name = "day") val day: Float?
    , @field:Json(name = "min") val min: Float?
    , @field:Json(name = "max") val max: Float?
    , @field:Json(name = "night") val night: Float?
    , @field:Json(name = "eve") val eve: Float?
    , @field:Json(name = "morn") val morn: Float?
)