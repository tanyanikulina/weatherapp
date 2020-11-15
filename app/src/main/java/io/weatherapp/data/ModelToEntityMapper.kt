package io.weatherapp.data

import io.realm.Realm
import io.realm.RealmList
import io.weatherapp.api.responses.*

class ModelToEntityMapper {

    private val realm: Realm = Realm.getDefaultInstance()

    fun convert(resp: ResponseWeatherModel) {

        realm.beginTransaction()

        val weatherEntity = realm.createObject(TotalWeather::class.java)
        weatherEntity.lat = resp.lat
        weatherEntity.lon = resp.lon
        weatherEntity.timezone = resp.timezone
        weatherEntity.timezoneOffset = resp.timezoneOffset
        resp.current?.let { weatherEntity.current = createCurrentWeather(it) }
        resp.hourly?.let { weatherEntity.hourly = createHourlyWeather(it) }
        resp.daily?.let { weatherEntity.daily = createDailyWeather(it) }

        realm.commitTransaction()
    }

    private fun createCurrentWeather(model: CurrentModel): CurrentWeather? {
        val weather = realm.createObject(CurrentWeather::class.java)
        weather.date = model.date
        weather.sunrise = model.sunrise
        weather.sunset = model.sunset
        weather.temp = model.temp
        weather.feelsLike = model.feelsLike
        weather.pressure = model.pressure
        weather.humidity = model.humidity
        weather.dewPoint = model.dewPoint
        weather.uvi = model.uvi
        weather.clouds = model.clouds
        weather.visibility = model.visibility
        weather.windSpeed = model.windSpeed
        weather.windDegrees = model.windDegrees
        model.weather?.let { weather.weather = createItemWeather(it) }
        model.rain?.let { weather.rain = createSnowRain(it) }
        model.snow?.let { weather.snow = createSnowRain(it) }
        return weather
    }

    private fun createHourlyWeather(models: List<HourlyModel>): RealmList<HourlyWeather>? {
        val list = RealmList<HourlyWeather>()
        models.forEach {
            val weather = realm.createObject(HourlyWeather::class.java)
            weather.date = it.date
            weather.temp = it.temp
            weather.feelsLike = it.feelsLike
            weather.pressure = it.pressure
            weather.humidity = it.humidity
            weather.dewPoint = it.dewPoint
            weather.clouds = it.clouds
            weather.visibility = it.visibility
            weather.windSpeed = it.windSpeed
            weather.windDegrees = it.windDegrees
            weather.windGust = it.windGust
            weather.pop = it.pop
            it.weather?.let { weather.weather = createItemWeather(it) }
            it.rain?.let { weather.rain = createSnowRain(it) }
            it.snow?.let { weather.snow = createSnowRain(it) }
            list.add(weather)
        }
        return list
    }

    private fun createDailyWeather(models: List<DailyModel>): RealmList<DailyWeather>? {
        val list = RealmList<DailyWeather>()
        models.forEach {
            val weather = realm.createObject(DailyWeather::class.java)
            weather.date = it.date
            weather.sunrise = it.sunrise
            weather.sunset = it.sunset
            weather.pressure = it.pressure
            weather.humidity = it.humidity
            weather.dewPoint = it.dewPoint
            weather.clouds = it.clouds
            weather.visibility = it.visibility
            weather.windSpeed = it.windSpeed
            weather.windDegrees = it.windDegrees
            weather.windGust = it.windGust
            weather.pop = it.pop
            it.temp?.let { weather.temp = createTemperature(it) }
            it.feelsLike?.let { weather.feelsLike = createTemperature(it) }
            it.weather?.let { weather.weather = createItemWeather(it) }
            it.rain?.let { weather.rain = createSnowRain(it) }
            it.snow?.let { weather.snow = createSnowRain(it) }
            list.add(weather)
        }
        return list
    }

    private fun createItemWeather(models: List<WeatherModel>): RealmList<ItemWeather>? {
        val list = RealmList<ItemWeather>()
        models.forEach {
            val weather = realm.createObject(ItemWeather::class.java)
            weather.id = it.id
            weather.main = it.main
            weather.description = it.description
            weather.icon = it.icon
            list.add(weather)
        }
        return list
    }

    private fun createTemperature(model: TemperatureModel): TemperatureWeather? {
        val weather = realm.createObject(TemperatureWeather::class.java)
        weather.day = model.day
        weather.min = model.min
        weather.max = model.max
        weather.night = model.night
        weather.eve = model.eve
        weather.morn = model.morn
        return weather
    }

    private fun createSnowRain(model: SnowRainModel): SnowRainWeather? {
        val weather = realm.createObject(SnowRainWeather::class.java)
        weather.h1 = model.h1
        weather.h3 = model.h3
        return weather
    }
}