package io.weatherapp.api

import com.squareup.moshi.JsonDataException
import io.weatherapp.api.responses.ResponseWeatherModel
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiRepository {

    val API_KEY = "7d31fa3d928db13433eed3fa0aef859a"

    val apiInterface by lazy { ApiFactory.generateApi() }

    suspend fun getOnecallWeatherLocation(
        lat: Double = 48.450001,
        lon: Double = 34.98333
    ): Result<Any>? {
        val call: suspend () -> Response<ResponseWeatherModel> =
            {
                val params = HashMap<String, String>()
                params.put("lat", lat.toString())
                params.put("lon", lon.toString())
                params.put("appid", API_KEY)
                params.put("units", "metric")
                params.put("exclude", "minutely")
//                params.put("lang", "ru")

                apiInterface.getOnecallWeather(params)
            }
        return safetyCall(call)
    }

    @JvmSuppressWildcards
    suspend fun <T : Any> safetyCall(
        call: suspend () -> Response<T>,
        requestPage: Int = PAGE_COMMON
    ): Result<Any>? {
        var response: Result<Any>?
        try {
            val result: Response<T> = call.invoke()
            if (result.isSuccessful) {
                response = Result.Success<T>(result.body()!!)
            } else {
                //todo
//                response = parseError(requestPage, result)
                response = Result.Error(result.code(), result.toString())
            }
        } catch (e: Exception) {
            response = Result.Error(errMsg = e.message ?: "")
            when (e) {
                is SocketTimeoutException -> response = Result.Error(ERROR_TIMEOUT, e.message ?: "")
                is UnknownHostException -> response =
                    Result.Error(ERROR_NO_INTERNET, e.message ?: "")
                is JsonDataException -> response = Result.Error(ERROR_JSON_PARSE, e.message ?: "")
            }
        }
        return response
    }

}