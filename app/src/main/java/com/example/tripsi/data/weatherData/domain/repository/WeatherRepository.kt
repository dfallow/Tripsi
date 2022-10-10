package com.example.tripsi.data.weatherData.domain.repository

import com.example.tripsi.data.weatherData.domain.util.Resource
import com.example.tripsi.data.weatherData.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}