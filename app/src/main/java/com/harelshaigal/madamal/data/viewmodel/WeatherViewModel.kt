package com.harelshaigal.madamal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.harelshaigal.madamal.data.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val _weatherData = MutableLiveData<WeatherData?>()
    private val client = OkHttpClient()
    val weatherData: LiveData<WeatherData?> = _weatherData
    private val ioDispatcher = Dispatchers.IO


    fun fetchWeather() {
//         On a Real device use this values, on emulator those are not true
//        val lat = LocationHelper.lat,
//        val lon = LocationHelper.lng,

        val url =
            "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=32.067624&lon=34.776054"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("Cache-Control", "no-cache")
            .addHeader("User-Agent", "MyWeatherApp/1.0 (contact@example.com)")
            .build()

        CoroutineScope(ioDispatcher).launch {
            var response: Response? = null
            try {
                response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string() ?: ""
                    val weatherData = Gson().fromJson(responseData, WeatherData::class.java)
                    _weatherData.postValue(weatherData)
                } else {
                    _weatherData.postValue(null)
                }
            } catch (e: IOException) {
                _weatherData.postValue(null)
            } finally {
                response?.close()
            }
        }
    }
}
