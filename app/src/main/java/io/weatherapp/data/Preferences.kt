package io.weatherapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.weatherapp.models.CityModel

class Preferences(val context: Context) {

    private val sp: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    private val CITY_NAME = "CITY_NAME"
    private val CITY_LAT = "CITY_LAT"
    private val CITY_LON = "CITY_LON"

    fun saveCity(city: CityModel) {
        sp.edit(commit = true) { putString(CITY_LAT, city.name) }
        sp.edit(commit = true) { putString(CITY_LON, city.lat) }
        sp.edit(commit = true) { putString(CITY_NAME, city.lon) }
    }

    fun getCity(): CityModel {
        val name = sp.getString(CITY_NAME, "Dnipro") ?: ""
        val lat = sp.getString(CITY_LAT, "48.450001") ?: ""
        val lon = sp.getString(CITY_LON, "34.98333") ?: ""
        return CityModel(name, lat, lon)
    }

}