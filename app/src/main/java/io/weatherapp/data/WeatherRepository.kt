package io.weatherapp.data

import io.realm.Realm
import io.weatherapp.api.responses.ResponseWeatherModel

class WeatherRepository {

    var realm: Realm = Realm.getDefaultInstance()

    fun saveNewWeather(resp: ResponseWeatherModel) {
        val v = realm.where(TotalWeather::class.java).findFirst()
        if (v != null) clearDatabase()
        ModelToEntityMapper().convert(resp)
    }

    fun getCurrentWeather(): CurrentWeather? {
        val v = realm.where(TotalWeather::class.java).findFirst()
        v?.let {
            return it.current
        }
        return null
    }

    fun getHourlyWeather(): List<HourlyWeather>? {
        val v = realm.where(TotalWeather::class.java).findFirst()
        v?.let {
            return it.hourly
        }
        return null
    }

    fun getDailyWeather(): List<DailyWeather>? {
        val v = realm.where(TotalWeather::class.java).findFirst()
        v?.let {
            return it.daily
        }
        return null
    }

    private fun clearDatabase() {
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

}