package com.example.powerhousetest.network

import com.example.powerhousetest.BuildConfig
import com.example.powerhousetest.model.WeatherResponse

class WeatherRepository {

    private val weatherApiService = RetrofitInstance.weatherApiService

    private  val apiKey = BuildConfig.OPENWEATHERMAP_API_KEY


    suspend fun getWeatherByLocation(lat: Double, lon: Double): WeatherResponse {
        return weatherApiService.getWeatherByLocation(lat, lon, apiKey)
    }

    suspend fun getWeatherByCityName(cityName:String): WeatherResponse {
        return weatherApiService.getWeatherByCityName(cityName, apiKey)
    }
}
