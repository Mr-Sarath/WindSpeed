package com.app.windspeed.remote


import com.app.windspeed.model.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

  /*  @Headers("Content-Type:application/json")
    @POST("auth/login")
    suspend fun loginApi(@Body info: LoginBody): Response<LoginResponse>
*/

    @Headers("Content-Type:application/json")
    @GET("weather?lat={lat}&lon={lon}&appid={API key}")
    suspend fun weatherapi(
/*
        @Header("Authorization") auth: String,
*/
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("appid") apikey:String
    ): Response<CurrentWeatherResponse>

}