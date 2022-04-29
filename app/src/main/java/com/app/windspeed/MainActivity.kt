package com.app.windspeed

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.app.windspeed.utils.*
import com.app.windspeed.utils.Constants.API_KEY
import com.app.windspeed.viewmodel.WeatherViewModel
import com.app.windspeed.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel by viewModels<WeatherViewModel>()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.locationText?.setOnClickListener {
            startGps()
        }
    }

    private fun startGps() {
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun getCurrentWeatherApi(lat: String, lng: String) {
        viewModel.weatherList(latitude = lat, longitude = lng, API_KEY)
        viewModel.weatherResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        binding?.progressW?.hide()
                        response.data?.let { res ->
                            if (res.cod == 200) {
                                res.name?.let {
                                    binding?.locationText?.text = it
                                }
                                res.main?.temp?.let {
                                    binding?.celciusText?.text = it.toString()
                                }
                                res.weather?.get(0)?.let { it->
                                    it.main.let {
                                        binding?.conditionText?.text = it
                                    }
                                  /*  when(it.icon.toString()){
                                        "01d" -> {
                                            binding?.conditionText
                                        }
                                    }*/
                                }


                            } else {
                                res.message?.let {
                                    shortToast(it)
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressW?.hide()

                        binding?.root?.errorSnackAction(
                            response.message ?: "Something went wrong, Please try again!"
                        )
                        {}
                    }
                    is Resource.Loading<*> -> {
                        binding?.progressW?.show()
                    }
                }
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        val lat = location.latitude.toString()
                        val lng = location.longitude.toString()
                        getCurrentWeatherApi(lat, lng)
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }

    /*   override fun onStart() {
           super.onStart()
           startGps()
       }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}