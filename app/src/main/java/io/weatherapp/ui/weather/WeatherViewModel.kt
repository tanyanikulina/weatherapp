package io.weatherapp.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.weatherapp.api.ApiRepository
import io.weatherapp.api.Result
import io.weatherapp.api.responses.ResponseWeatherModel
import io.weatherapp.data.AbstractWeather
import io.weatherapp.data.HourlyWeather
import io.weatherapp.data.Preferences
import io.weatherapp.data.WeatherRepository
import io.weatherapp.models.CityModel
import io.weatherapp.models.DayModel
import io.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class WeatherViewModel(val pref: Preferences) : ViewModel() {

    val parentJob = Job()
    val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    val scope = CoroutineScope(coroutineContext)

    val cityName = MutableLiveData<String>()
    val currentWeather = MutableLiveData<AbstractWeather>()
    val dailyWeather = MutableLiveData<List<DayModel>>()
    val hourlyWeather = MutableLiveData<List<HourlyWeather>>()
    val errorMsg = SingleLiveEvent<String>()

    private val repo = WeatherRepository()

    init {
        val city = pref.getCity()
        cityName.postValue(city.name)
        getWeatherLocation(city.lat!!, city.lon!!)
    }

    private fun getWeatherLocation(lat: String, lon: String) {

        scope.launch {
            val resp = ApiRepository().getOnecallWeatherLocation(lat, lon)
            when (resp) {
                is Result.Success -> {
                    scope.launch(Dispatchers.Main) {
                        val answer = resp.data as ResponseWeatherModel
                        repo.saveNewWeather(answer)
                        loadWeatherFromRepository()
                    }
                }
                is Result.Error -> {
                    errorMsg.postValue(resp.errMsg)
                    scope.launch(Dispatchers.Main) {
                        loadWeatherFromRepository()
                    }
                }
            }
        }
    }

    fun saveNewCity(city: CityModel) {
        pref.saveCity(city)
        cityName.postValue(city.name)
        getWeatherLocation(city.lat!!, city.lon!!)
    }

    fun dayClicked(position: Int) {

        val list = ArrayList<DayModel>()
        list.addAll(dailyWeather.value as List<DayModel>)
        if (list.size > position + 1) {
            list.forEach { it.isSelected = false }
            list[position].isSelected = true
        }
        dailyWeather.postValue(list)
        currentWeather.postValue(list[position].dayWeather)
    }

    private fun loadWeatherFromRepository() {

        repo.getDailyWeather()?.let { list ->
            val listDailyWeather = ArrayList<DayModel>()
            list.forEach {
                listDailyWeather.add(DayModel(it, false))
            }
            if (listDailyWeather.size > 0) {
                listDailyWeather[0].isSelected = true
            }
            dailyWeather.postValue(listDailyWeather)
        }

        currentWeather.postValue(repo.getCurrentWeather())
        hourlyWeather.postValue(repo.getHourlyWeather())
    }

    fun refreshClicked() {
        val city = pref.getCity()
        getWeatherLocation(city.lat!!, city.lon!!)
    }

}