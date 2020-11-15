package io.weatherapp.api

import io.weatherapp.api.responses.ResponseWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("onecall")
    suspend fun getOnecallWeather(@QueryMap params: Map<String, String>): Response<ResponseWeatherModel>

}