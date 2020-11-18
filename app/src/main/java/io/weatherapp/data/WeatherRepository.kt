package io.weatherapp.data

import io.realm.Realm
import io.weatherapp.api.responses.ResponseWeatherModel

class WeatherRepository {

    var realm: Realm = Realm.getDefaultInstance()

    fun saveNewWeather(resp: ResponseWeatherModel) {
        val v = realm.where(TotalWeather::class.java).findFirst()
        if (v != null) clearTotalWeather()
        ModelToEntityMapper().convertWeather(resp)
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

    fun initCities(): List<CityEntity>? {
        val v = realm.where(CityEntity::class.java).findFirst()
        if (v == null) {
            realm.beginTransaction()

            addCity("Dnepr", 48.450001, 34.98333)
            addCity("Днепр", 48.450001, 34.98333)
            addCity("Verkhnedneprovsk", 48.65242, 34.334572)
            addCity("Верхнеднепровск", 48.65242, 34.334572)
            addCity("Dnepropetrovska Oblast'", 48.5, 35.0)
            addCity("Днепропетровская область", 48.5, 35.0)
            addCity("Kyiv", 50.433334, 30.516666)
            addCity("Киев", 50.433334, 30.516666)
            addCity("Zaporizhia", 35.183331, 47.816669)
            addCity("Запорожье", 35.183331, 47.816669)
            addCity("Zaprudnoye", 44.599998, 34.316666)
            addCity("Запрудное", 44.599998, 34.316666)
            addCity("Zaporiz’ka Oblast’", 47.5, 35.5)
            addCity("Запорожская область", 47.5, 35.5)

            realm.commitTransaction()
        }
        return realm.where(CityEntity::class.java).findAll()
    }

    private fun addCity(name: String, lat: Double, lon: Double) {
        val city = realm.createObject(CityEntity::class.java)
        city.name = name
        city.lat = lat.toFloat()
        city.lon = lon.toFloat()
    }

    private fun clearTotalWeather() {
        realm.beginTransaction()
        val b = realm.where(TotalWeather::class.java).findAll().deleteAllFromRealm()
        realm.commitTransaction()
    }

    private fun clearDatabase() {
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
    }

}