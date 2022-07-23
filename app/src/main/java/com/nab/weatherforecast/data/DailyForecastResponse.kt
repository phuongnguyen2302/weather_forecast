package com.nab.weatherforecast.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyForecastResponse(
    @field:Json(name = "list")
    val dailyList: List<DailyForecastDto>,
)