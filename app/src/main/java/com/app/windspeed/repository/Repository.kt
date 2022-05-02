package com.app.windspeed.repository

import com.app.windspeed.remote.ApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun weather(latitude: Double, longitude: Double, apikey: String) =
        apiService.weatherapi(
            latitude.toInt(),
            longitude.toInt(),
            apikey
        )
}