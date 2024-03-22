package com.example.powerhousetest.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powerhousetest.model.WeatherResponse
import com.example.powerhousetest.network.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import com.example.powerhousetest.utils.CachedMechanism
import com.google.gson.Gson


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WeatherRepository()
    private val _weatherData = MutableLiveData<List<WeatherResponse>>()


    // LiveData for the weather response
    val weatherData : LiveData<List<WeatherResponse>> =  _weatherData

    val cachedMechanism = CachedMechanism(application.applicationContext)
    private val _cachedWeatherData = MutableLiveData<List<WeatherResponse>>()
    val cachedWeatherData: LiveData<List<WeatherResponse>> = _cachedWeatherData

    private val gson = Gson()



    fun getWeather(lat: Double, lon: Double, cities: List<String>) {
        viewModelScope.launch {
            try {

                val cachedData = cachedMechanism.getCachedData()
                if (cachedData != null && cachedMechanism.isDataValid(cachedData.timestamp)) {
                    // Use cached data
                    _cachedWeatherData.value = cachedData.weatherResponse
                } else {
                    val weatherLocationData = repository.getWeatherByLocation(lat, lon)
                    val weatherCityData = getCityWeatherData(cities)
                    val combinedWeatherData = mutableListOf<WeatherResponse>()
                    combinedWeatherData.add(weatherLocationData)
                    combinedWeatherData.addAll(weatherCityData)
//                    _weatherData.value = combinedWeatherData
                    _cachedWeatherData.value = combinedWeatherData
                    cachedMechanism.saveDataToCache(combinedWeatherData)
                }

//                Log.d("_weatherData", _weatherData.value.toString());

            } catch (e: Exception) {
                // Handle error
//                Log.d("_weatherData", e.message.toString());
            }
        }
    }


    private suspend fun getCityWeatherData(cities: List<String>): List<WeatherResponse> {
        val cityWeatherData = mutableListOf<WeatherResponse>()
            for (city in cities) {
                val cityWeather = repository.getWeatherByCityName(city)
                cityWeatherData.add(cityWeather)
            }
        return cityWeatherData
    }
}
