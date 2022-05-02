package com.app.windspeed.remote


import com.app.windspeed.model.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

  /*  @Headers("Content-Type:application/json")
    @POST("auth/login")
    suspend fun loginApi(@Body info: LoginBody): Response<LoginResponse>
*/
/*
    @GET("weather?lat={lat}&lon={lon}&appid={API key}")
    suspend fun weatherapi(
        @Path("lat") latitude:String,
        @Path("lon") longitude:String,
        @Path("appid") apikey:String
    ): Response<CurrentWeatherResponse>
*/

//    @Headers("Content-Type:application/json")
    @GET("data/2.5/weather?")
    suspend fun weatherapi(
    @Query("lat") latitude: Int,
    @Query("lon") longitude: Int,
    @Query("appid") apikey: String,
    @Query("units") unit: String="metric"
    ): Response<CurrentWeatherResponse>

}