package com.app.windspeed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.windspeed.model.CurrentWeatherResponse
import com.app.windspeed.repository.Repository
import com.app.windspeed.utils.Constants
import com.app.windspeed.utils.Event
import com.app.windspeed.utils.Resource
import com.app.windspeed.utils.ResponseCodeManager.checkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject
constructor(private val repository: Repository) : ViewModel() {

    private val mWeatherResponse: MutableLiveData<Event<Resource<CurrentWeatherResponse>>> =
        MutableLiveData()
    val weatherResponse: LiveData<Event<Resource<CurrentWeatherResponse>>>
        get() = mWeatherResponse


    fun weatherList(latitude: String, longitude: String, apikey: String) =
        viewModelScope.launch(Dispatchers.IO) {
            mWeatherResponse.postValue(Event(Resource.Loading()))
            try {
                val response = repository.weather(
                    latitude,
                    longitude,
                    apikey
                )

                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        mWeatherResponse.postValue(Event(Resource.Success(resultResponse)))
                    }
                } else {
                    mWeatherResponse.postValue(
                        Event(
                            Resource.Error(
                                checkResponse(response)
                            )
                        )
                    )
                }
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> mWeatherResponse.postValue(
                        Event(Resource.Error(Constants.NETWORK_FAILURE))
                    )
                    else -> mWeatherResponse.postValue(
                        Event(Resource.Error(Constants.CONVERSION_FAILURE))
                    )
                }
            }
        }

}