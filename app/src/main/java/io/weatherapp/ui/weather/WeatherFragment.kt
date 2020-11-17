package io.weatherapp.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.weatherapp.R
import io.weatherapp.data.AbstractWeather
import io.weatherapp.data.Preferences
import io.weatherapp.models.CityModel
import io.weatherapp.ui.base.BaseFragment
import io.weatherapp.ui.base.ViewModelFactory
import io.weatherapp.utils.DateUtils
import io.weatherapp.utils.DateUtils.DF_WEEK_DAY_MONTH
import io.weatherapp.utils.ImageUtils
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : BaseFragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel
    private lateinit var dayAdapter: DayAdapter
    private lateinit var hourAdapter: HourAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sp = Preferences(requireContext())
        viewModel = ViewModelProvider(this, ViewModelFactory(sp)).get(WeatherViewModel::class.java)

        hourAdapter = HourAdapter()
        dayAdapter = DayAdapter {
            viewModel.dayClicked(it)
        }

        rvDaily.adapter = dayAdapter
        rvHourly.adapter = hourAdapter
        showLoading(true)

        viewModel.cityName.observe {
            tvCityName.text = it
        }
        viewModel.currentWeather.observe {
            showLoading(false)
            fillMainWeather(it)
        }
        viewModel.dailyWeather.observe {
            dayAdapter.setNewData(it)
        }
        viewModel.hourlyWeather.observe {
            hourAdapter.setNewData(it)
        }
        viewModel.errorMsg observe {
            showLoading(false)
            showDialog(requireContext(), null, getString(R.string.error_load))
        }

        ivLocation.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_mapsFragment)
        }

        rlSwipe.setOnRefreshListener {
            showLoading(true)
            viewModel.refreshClicked()
        }

        checkBackStack()
    }

    private fun fillMainWeather(weather: AbstractWeather) {

        tvDate.text =
            DateUtils.formatDateFromMillis(weather.receiveDateMillis(), DF_WEEK_DAY_MONTH)

        tvTemp.text = weather.receiveTemp()
        tvHumidity.text = weather.receiveHumidity()
        tvWind.text = getString(R.string.wind_speed, weather.receiveWindSpeed())

        ivWind.setImageDrawable(
            resources.getDrawable(ImageUtils.getWindRes(weather.receiveWindDegrees()))
        )
        ivMainWeather.setImageDrawable(
            resources.getDrawable(ImageUtils.getWeatherRes(weather.receiveWeather()))
        )
    }

    private fun showLoading(isLoading: Boolean) {
        clTop.isEnabled = !isLoading
        pb.isVisible = isLoading
        if (!isLoading)
            rlSwipe.isRefreshing = false
    }

    private fun checkBackStack() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<CityModel>(
            "location"
        )
            ?.observe {
                val city = CityModel(it.name, it.lat, it.lon)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<CityModel>(
                    "location"
                )

                showLoading(true)
                viewModel.saveNewCity(city)
            }
    }

}