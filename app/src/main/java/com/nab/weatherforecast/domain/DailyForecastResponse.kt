package com.nab.weatherforecast.domain

sealed class DailyForecastResponse {
    data class Success(val list: List<DailyForecast>) : DailyForecastResponse()
    data class Error(val message: String) : DailyForecastResponse()
    data class Empty(val message: String) : DailyForecastResponse()
}