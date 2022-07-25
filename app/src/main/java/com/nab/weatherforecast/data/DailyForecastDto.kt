package com.nab.weatherforecast.data

import com.nab.weatherforecast.domain.DailyForecast
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyForecastDto(
    @field:Json(name = "dt")
    val date: Long,

    @field:Json(name = "temp")
    val temperature: DailyForecastTemperatureDto,

    @field:Json(name = "pressure")
    val pressure: Int,

    @field:Json(name = "humidity")
    val humidity: Int,

    @field:Json(name = "weather")
    val weatherDescription: List<DailyForecastDescriptionDto>,
) {
    fun toDailyForecast(): DailyForecast {
        return DailyForecastMapper.map(this)
    }
}

@JsonClass(generateAdapter = true)
data class DailyForecastTemperatureDto(
    @field:Json(name = "day")
    val averageTemperature: Double,
)

@JsonClass(generateAdapter = true)
data class DailyForecastDescriptionDto(
    @field:Json(name = "description")
    val description: String,
)