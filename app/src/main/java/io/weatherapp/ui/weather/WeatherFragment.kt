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
        pb.isVisible = true

        viewModel.cityName.observe {
            tvCityName.text = it
        }
        viewModel.currentWeather.observe {
            pb.isVisible = false
            fillMainWeather(it)
        }
        viewModel.dailyWeather.observe {
            dayAdapter.setNewData(it)
        }
        viewModel.hourlyWeather.observe {
            hourAdapter.setNewData(it)
        }
        viewModel.errorMsg observe {
            pb.isVisible = false
            showDialog(requireContext(), null, it)
        }

        ivLocation.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_mapsFragment)
        }
        checkBackStack()
    }

    private fun fillMainWeather(weather: AbstractWeather) {

        tvDate.text =
            DateUtils.formatDateFromMillis(weather.receiveDateMillis(), DF_WEEK_DAY_MONTH)

        tvTemp.text = weather.receiveTemp()
//        tvHumidity.text = getString(R.string.humidity, weather.receiveHumidity())
        tvHumidity.text = weather.receiveHumidity() + "%"
        tvWind.text = getString(R.string.wind_speed, weather.receiveWindSpeed())

        ivWind.setImageDrawable(
            resources.getDrawable(ImageUtils.getWindRes(weather.receiveWindDegrees()))
        )
        ivMainWeather.setImageDrawable(
            resources.getDrawable(ImageUtils.getWeatherRes(weather.receiveWeather()))
        )
    }

    private fun checkBackStack() {
        var city: CityModel

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Triple<String, String, String>>(
            "location"
        )
            ?.observe {
                city = CityModel(it.first, it.second, it.third)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Triple<String, String, String>>(
                    "location"
                )

                pb.isVisible = true
                viewModel.saveNewCity(city)
            }
    }

}