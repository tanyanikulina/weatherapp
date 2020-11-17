package io.weatherapp.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.weatherapp.api.ApiRepository
import io.weatherapp.api.Result
import io.weatherapp.api.responses.ResponseWeatherModel
import io.weatherapp.data.*
import io.weatherapp.models.CityModel
import io.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherViewModel(val pref: Preferences) : ViewModel() {

    val cityName = MutableLiveData<String>()
    val currentWeather = MutableLiveData<CurrentWeather>()
    val dailyWeather = MutableLiveData<List<DailyWeather>>()
    val hourlyWeather = MutableLiveData<List<HourlyWeather>>()
    val errorMsg = SingleLiveEvent<String>()

    private val repo = WeatherRepository()

    init {
        val city = pref.getCity()
        cityName.postValue(city.name)
        getWeatherLocation(city.lat, city.lon)
    }

    fun getWeatherLocation(lat: String, lon: String) {

        GlobalScope.launch {
            val resp = ApiRepository().getOnecallWeatherLocation(lat, lon)
            when (resp) {
                is Result.Success -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        val answer = resp.data as ResponseWeatherModel
                        repo.saveNewWeather(answer)
                        loadWeatherFromRepository()
                    }
                }
                is Result.Error -> {
                    //todo process error and show msg "Не удалось загрузить новые данные :("
                    errorMsg.postValue(resp.errMsg)
                    GlobalScope.launch(Dispatchers.Main) {
                        loadWeatherFromRepository()
                    }
                }
            }
        }
    }

    fun saveNewCity(city: CityModel) {
        pref.saveCity(city)
        cityName.postValue(city.name)
        getWeatherLocation(city.lat, city.lon)
    }

    private fun loadWeatherFromRepository() {
        currentWeather.postValue(repo.getCurrentWeather())
        dailyWeather.postValue(repo.getDailyWeather())
        hourlyWeather.postValue(repo.getHourlyWeather())
    }

}