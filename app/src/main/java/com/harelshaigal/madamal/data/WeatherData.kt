package com.harelshaigal.madamal.data

data class WeatherData(val properties: WeatherProperties)
data class WeatherProperties(val timeseries: List<WeatherTimeseries>)
data class WeatherTimeseries(val data: WeatherDataInner)
data class WeatherDataInner(val instant: WeatherInstant)
data class WeatherInstant(val details: WeatherDetails)
data class WeatherDetails(val air_temperature: Double)
