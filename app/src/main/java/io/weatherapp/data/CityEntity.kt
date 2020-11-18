package io.weatherapp.data

import io.realm.RealmObject

open class CityEntity(
    var name: String? = null
    , var lat: Float? = null
    , var lon: Float? = null
) : RealmObject()