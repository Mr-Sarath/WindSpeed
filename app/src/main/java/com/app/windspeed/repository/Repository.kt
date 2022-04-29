package com.app.windspeed.repository

import com.app.windspeed.remote.ApiService
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun weather(latitude: String, longitude: String, apikey: String) =
        apiService.weatherapi(
            latitude,
            longitude,
            apikey
        )
}