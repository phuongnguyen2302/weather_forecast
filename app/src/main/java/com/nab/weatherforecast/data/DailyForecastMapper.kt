package com.nab.weatherforecast.data

import com.nab.weatherforecast.domain.DailyForecast
import java.text.SimpleDateFormat
import java.util.*

object DailyForecastMapper {
    fun map(dailyForecastDto: DailyForecastDto): DailyForecast {
        val formatDate = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
        return DailyForecast(
            date = formatDate.format(Date(dailyForecastDto.date * 1000)),
            temperature = dailyForecastDto.temperature.averageTemperature,
            pressure = dailyForecastDto.pressure,
            humidity = dailyForecastDto.humidity,
            weatherDescription = dailyForecastDto.weatherDescription.description
        )
    }
}