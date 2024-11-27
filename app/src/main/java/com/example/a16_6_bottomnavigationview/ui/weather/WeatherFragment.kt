package com.example.a16_6_bottomnavigationview.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a16_6_bottomnavigationview.R
import com.example.a16_6_bottomnavigationview.databinding.FragmentWeatherBinding
import com.example.a16_6_bottomnavigationview.models.CurrentWeather
import com.example.a16_6_bottomnavigationview.utils.RetrofitInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            showSnackbar(false, getString(R.string.permission_denied))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getWeatherBTN.setOnClickListener{
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    getWeatherByLocation(location.latitude, location.longitude)
                } else {
                    showSnackbar(false, getString(R.string.error_location_not_found))
                }
            }
        }
    }

    private fun getWeatherByLocation(latitude: Double, longitude: Double) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeatherByLocation(
                    latitude,
                    longitude,
                    "metric",
                    getString(R.string.apiKey),
                    "ru"
                )
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    showSnackbar(false, getString(R.string.error_app, e.message))
                }
                return@launch
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showSnackbar(false, getString(R.string.error_http, e.message))
                }
                return@launch
            }

            withContext(Dispatchers.Main) {
                clearFields()
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val data = response.body()!!
                    updateWeatherUI(data)
                    showSnackbar(true, getString(R.string.data_received))
                }
            } else {
                withContext(Dispatchers.Main) {
                    showSnackbar(false, getString(R.string.error_city_not_found))
                }
            }
        }
    }

    private fun clearFields() {
        binding.cityTV.text = "Город"
        binding.minTempTV.text = "мин t"
        binding.maxTempTV.text = "макс t"
        val imageURL = R.drawable.error
        Picasso.get().load(imageURL).into(binding.weatherIW)
        binding.windDirectionTV.text = "Направление ветра"
        binding.windSpeedTV.text = "Скорость ветра"
        binding.pressureTV.text = "Давление"
        binding.humidityTV.text = "Влажность"
    }

    private fun updateWeatherUI(data: CurrentWeather) {
        binding.cityTV.text = data.name
        binding.minTempTV.text = getString(R.string.min_temp, data.main.temp_min.toInt())
        binding.maxTempTV.text = getString(R.string.max_temp, data.main.temp_max.toInt())
        binding.windDirectionTV.text = getString(R.string.wind_direction, data.wind.deg)
        binding.windSpeedTV.text = getString(R.string.wind_speed, data.wind.speed.toInt())
        val pressure = (data.main.pressure / 1.33).toInt()
        binding.pressureTV.text = getString(R.string.pressure, pressure)
        binding.humidityTV.text = getString(R.string.humidity, data.main.humidity)
        val iconId = data.weather[0].icon
        val imageURL = "https://openweathermap.org/img/wn/$iconId@4x.png"
        Picasso.get().load(imageURL).into(binding.weatherIW)
    }

    private fun showSnackbar(type: Boolean, message: String) {
        view?.let {
            if (!type) {
                Log.e("ERROR", message)
            } else {
                Log.d("SUCCESS", message)
            }
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
