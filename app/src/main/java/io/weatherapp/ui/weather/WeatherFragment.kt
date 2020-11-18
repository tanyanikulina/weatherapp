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
import io.weatherapp.utils.DF_WEEK_DAY_MONTH
import io.weatherapp.utils.DateUtils
import io.weatherapp.utils.ImageUtils
import io.weatherapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_weather.*

const val LOCATION_ARG = "location"

class WeatherFragment : BaseFragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel
    private lateinit var dayAdapter: DayAdapter
    private lateinit var hourAdapter: HourAdapter
    private lateinit var cityAdapter: CityAdapter

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

        viewModel.cityName observe {
            tvCityName.text = it
        }
        viewModel.cityList.observe {
            cityAdapter = CityAdapter(it)
            setupSearchCityField()
        }
        viewModel.currentWeather observe {
            showLoading(false)
            fillMainWeather(it)
        }
        viewModel.dailyWeather observe {
            dayAdapter.setNewData(it)
        }
        viewModel.hourlyWeather observe {
            hourAdapter.setNewData(it)
        }
        viewModel.errorMsg observe {
            showLoading(false)
            showDialog(requireContext(), null, getString(R.string.error_load))
        }

        ivLocation.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_mapsFragment)
        }
        tvCityName.setOnClickListener {
            tvCityName.isVisible = false
            tilSearch.isVisible = true
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

    private fun setupSearchCityField() {

        ctvSearch.threshold = 3
        ctvSearch.setAdapter(cityAdapter)
        ctvSearch.setOnItemClickListener { parent, view, position, id ->

            val city = cityAdapter.getItem(position)

            tvCityName.text = city.name
            tvCityName.isVisible = true
            tilSearch.isVisible = false

            ctvSearch.hideKeyboard()
            showLoading(true)
            viewModel.saveNewCity(city)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        clTop.isEnabled = !isLoading
        pb.isVisible = isLoading
        if (!isLoading)
            rlSwipe.isRefreshing = false
    }

    private fun checkBackStack() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<CityModel>(
            LOCATION_ARG
        )
            ?.observe {
                val city = CityModel(it.name, it.lat, it.lon)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<CityModel>(
                    LOCATION_ARG
                )

                showLoading(true)
                viewModel.saveNewCity(city)
            }
    }

}