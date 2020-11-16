package io.weatherapp.api

val RESPONSE_OK = 200
val RESPONSE_VALUE_ERROR = 400
val RESPONSE_AUTH_ERROR = 401
val RESPONSE_NOT_FOUND_ERROR = 404
val RESPONSE_TOO_LARGE = 413
val ERROR_WRONG_METHOD = 405
val ERROR_TIMEOUT = 408
val ERROR_NO_INTERNET = 499
val RESPONSE_SERVER_ERROR = 500
val RESPONSE_SERVER_NOT_WORKING = 503

val PAGE_COMMON = 0
val ERROR_JSON_PARSE = -5

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorCode: Int = -1, var errMsg: String = "") : Result<Nothing>()
}