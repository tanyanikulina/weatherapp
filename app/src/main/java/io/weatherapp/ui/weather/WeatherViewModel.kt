package io.weatherapp.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.weatherapp.api.ApiRepository
import io.weatherapp.api.Result
import io.weatherapp.api.responses.ResponseWeatherModel
import io.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    val weatherRespString = MutableLiveData<String>()
    val weatherModel = MutableLiveData<ResponseWeatherModel>()
    val errorMsg = SingleLiveEvent<String>()

    fun getWeatherLocation(lat: String = "48.450001", lon: String = "34.98333") {

        GlobalScope.launch {
            val resp = ApiRepository().getOnecallWeatherLocation()
            when (resp) {
                is Result.Success -> {
                    val answer = resp.data as ResponseWeatherModel
                    weatherModel.postValue(answer)
                }
                is Result.Error -> {
                    errorMsg.postValue(resp.errMsg)
                }
            }

            weatherRespString.postValue(resp.toString())
        }
    }

}