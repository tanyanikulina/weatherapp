package io.weatherapp.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.weatherapp.api.ApiRepository
import io.weatherapp.api.Result
import io.weatherapp.api.responses.ResponseWeatherModel
import io.weatherapp.data.CurrentWeather
import io.weatherapp.data.DailyWeather
import io.weatherapp.data.HourlyWeather
import io.weatherapp.data.WeatherRepository
import io.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    val weatherRespString = MutableLiveData<String>()
    val weatherModel = MutableLiveData<ResponseWeatherModel>()
    val currentWeather = MutableLiveData<CurrentWeather>()
    val dailyWeather = MutableLiveData<List<DailyWeather>>()
    val hourlyWeather = MutableLiveData<List<HourlyWeather>>()
    val errorMsg = SingleLiveEvent<String>()

    val repo = WeatherRepository()

    fun getWeatherLocation(lat: String = "48.450001", lon: String = "34.98333") {

        GlobalScope.launch {
            val resp = ApiRepository().getOnecallWeatherLocation()
            when (resp) {
                is Result.Success -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        val answer = resp.data as ResponseWeatherModel
                        repo.saveNewWeather(answer)
                        weatherModel.postValue(answer)
                        currentWeather.postValue(repo.getCurrentWeather())
                        dailyWeather.postValue(repo.getDailyWeather())
                        hourlyWeather.postValue(repo.getHourlyWeather())
                    }
                }
                is Result.Error -> {
                    errorMsg.postValue(resp.errMsg)
                }
            }

            weatherRespString.postValue(resp.toString())
        }
    }

}