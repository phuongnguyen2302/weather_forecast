package com.nab.weatherforecast.domain

data class DailyForecast(
    val date: String,
    val temperature: Double,
    val pressure: Int,
    val humidity: Int,
    val weatherDescription: String,
)