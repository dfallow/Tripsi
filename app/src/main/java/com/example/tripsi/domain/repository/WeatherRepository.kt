package com.example.tripsi.domain.repository

import com.example.tripsi.domain.util.Resource
import com.example.tripsi.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}