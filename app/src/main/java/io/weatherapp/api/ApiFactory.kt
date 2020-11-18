package io.weatherapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun generateApi(): ApiInterface = retrofit().create(ApiInterface::class.java)

    fun retrofit(): Retrofit {
        val baseUrl = BASE_URL
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().apply {
            readTimeout(40, TimeUnit.SECONDS)
            writeTimeout(40, TimeUnit.SECONDS)
            connectTimeout(40, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val request = requestBuilder.build()
                val response = chain.proceed(request)
                response
            }
        }

        return Retrofit.Builder()
            .client(client.build())
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


}