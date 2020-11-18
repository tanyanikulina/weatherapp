package io.weatherapp.data

import io.weatherapp.models.CityModel

class EntityToModelMapper {

    fun convertCities(list: List<CityEntity>): List<CityModel> {
        val cities = ArrayList<CityModel>()
        list.forEach {
            cities.add(CityModel(it.name, it.lat.toString(), it.lon.toString()))
        }
        return cities
    }

}