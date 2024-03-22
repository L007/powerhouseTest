package com.example.powerhousetest.utils

import android.content.Context
import com.example.powerhousetest.model.CachedWeatherData
import com.example.powerhousetest.model.WeatherResponse
import com.google.gson.Gson

class CachedMechanism(context:Context) {

    val sharedPreferences = context.getSharedPreferences("my_shared_prefs", Context.MODE_PRIVATE)

     fun saveDataToCache(data: List<WeatherResponse>) {
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit().apply {
            putString("cached_data", Gson().toJson(data))
            putLong("timestamp", currentTime)
            apply()
        }
    }

     fun getCachedData(): CachedWeatherData? {
        val cachedDataString = sharedPreferences.getString("cached_data", null)
        val timestamp = sharedPreferences.getLong("timestamp", 0)
        if (cachedDataString != null) {
            val cachedData = Gson().fromJson(cachedDataString, Array<WeatherResponse>::class.java).toList()
            return CachedWeatherData(cachedData, timestamp)
        }
        return null
    }

     fun isDataValid(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - timestamp
        val oneHourInMillis = 3600 * 1000 // 1 hour in milliseconds
        return elapsedTime < oneHourInMillis
    }

}