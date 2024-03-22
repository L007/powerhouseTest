package com.example.powerhousetest.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.powerhousetest.model.WeatherResponse
import com.example.powerhousetest.utils.CachedMechanism
import com.example.powerhousetest.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Manifest


@SuppressLint("MissingPermission")
@Composable
fun WeatherScreen(viewModel: MainViewModel) {
    var isLoading by remember { mutableStateOf(true) }

    val weatherData by viewModel.cachedWeatherData.observeAsState()
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    val cities = listOf("New York", "Singapore", "Mumbai", "Delhi", "Sydney", "Melbourne", "Jakarta", "Bangkok", "Istanbul", "Amsterdam")


    fusedLocationClient.lastLocation
        .addOnSuccessListener { location : Location? ->
            location?.let { viewModel.getWeather(it.latitude, location.longitude, cities) }
            isLoading = false

        }

    // Loading indicator
    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Getting your current location...", style = MaterialTheme.typography.bodyMedium)
        }
    } else{
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Text(
                    text = "Weather Information",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            weatherData?.forEach { weather ->
                item {
                    WeatherItem(weather = weather)
                }
            }
        }
    }
}

@Composable
fun WeatherItem(weather: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Location: ${weather.name}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Temperature: ${weather.main.temp}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Weather: ${weather.weather.firstOrNull()?.description}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}








