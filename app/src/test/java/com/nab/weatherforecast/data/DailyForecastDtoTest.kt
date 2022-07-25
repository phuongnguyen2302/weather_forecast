package com.nab.weatherforecast.data

import com.nab.weatherforecast.domain.DailyForecast
import io.mockk.mockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before

import org.junit.Test

class DailyForecastDtoTest {

    private lateinit var sut : DailyForecastDto

    @Before
    fun setUp() {
        sut = DailyForecastDto(
            date = 1234,
            temperature = DailyForecastTemperatureDto(10.0),
            pressure = 6,
            humidity = 10,
            weatherDescription = listOf(DailyForecastDescriptionDto("rainy"))
        )
    }

    @Test
    fun toDailyForecast() {
        // When
        val result = sut.toDailyForecast()

        // Then
        assertThat(result).isEqualTo(DailyForecast(
            date = "Thu, 01 Jan 1970",
            temperature = 10.0,
            pressure = 6,
            humidity = 10,
            weatherDescription = "rainy"
        ))
    }

    @Test
    fun `toDailyForecast - then call mapper`() {
        // When
        mockkObject(DailyForecastMapper)
        sut.toDailyForecast()

        // Then
        verify(exactly = 1) { DailyForecastMapper.map(sut) }
    }
}