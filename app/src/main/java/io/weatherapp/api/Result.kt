package io.weatherapp.api

const val ERROR_TIMEOUT = 408
const val ERROR_NO_INTERNET = 499
const val ERROR_JSON_PARSE = -5

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorCode: Int = -1, var errMsg: String = "") : Result<Nothing>()
}