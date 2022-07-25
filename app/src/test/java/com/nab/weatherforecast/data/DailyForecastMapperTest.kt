package com.nab.weatherforecast.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class DailyForecastMapperTest {

    private lateinit var sut: DailyForecastMapper

    @Before
    fun setUp() {
        sut = DailyForecastMapper
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `map - date timestamp should be converted to readable date`() {
        // Given
        val dailyForecastDto = DailyForecastDto(
            date = 1658635200,
            temperature = DailyForecastTemperatureDto( 0.0),
            pressure = 0,
            humidity = 0,
            weatherDescription = listOf(DailyForecastDescriptionDto("")),
        )

        // When
        val result = sut.map(dailyForecastDto)

        // Then
        assertThat(result.date).isEqualTo("Sun, 24 Jul 2022")
    }

    @Test
    fun `map - other date timestamp should be converted to readable date`() {
        // Given
        val dailyForecastDto = DailyForecastDto(
            date = 1658721600,
            temperature = DailyForecastTemperatureDto( 0.0),
            pressure = 0,
            humidity = 0,
            weatherDescription = listOf(DailyForecastDescriptionDto("")),
        )

        // When
        val result = sut.map(dailyForecastDto)

        // Then
        assertThat(result.date).isEqualTo("Mon, 25 Jul 2022")
    }

    @Test
    fun `map - temperature is passed from dto`() {
        // Given
        val dailyForecastDto = DailyForecastDto(
            date = 1658635200,
            temperature = DailyForecastTemperatureDto( 30.93),
            pressure = 0,
            humidity = 0,
            weatherDescription = listOf(DailyForecastDescriptionDto("")),
        )

        // When
        val result = sut.map(dailyForecastDto)

        // Then
        assertThat(result.temperature).isEqualTo(30.93)
    }

    @Test
    fun `map - pressure is passed from dto`() {
        // Given
        val dailyForecastDto = DailyForecastDto(
            date = 1658635200,
            temperature = DailyForecastTemperatureDto( 0.0),
            pressure = 1008,
            humidity = 0,
            weatherDescription = listOf(DailyForecastDescriptionDto("")),
        )

        // When
        val result = sut.map(dailyForecastDto)

        // Then
        assertThat(result.pressure).isEqualTo(1008)
    }

    @Test
    fun `map - humidity is passed from dto`() {
        // Given
        val dailyForecastDto = DailyForecastDto(
            date = 1658635200,
            temperature = DailyForecastTemperatureDto( 0.0),
            pressure = 0,
            humidity = 60,
            weatherDescription = listOf(DailyForecastDescriptionDto("")),
        )

        // When
        val result = sut.map(dailyForecastDto)

        // Then
        assertThat(result.humidity).isEqualTo(60)
    }

    @Test
    fun `map - description is passed from dto`() {
        // Given
        val dailyForecastDto = DailyForecastDto(
            date = 1658635200,
            temperature = DailyForecastTemperatureDto( 0.0),
            pressure = 0,
            humidity = 0,
            weatherDescription = listOf(DailyForecastDescriptionDto("light rain")),
        )

        // When
        val result = sut.map(dailyForecastDto)

        // Then
        assertThat(result.weatherDescription).isEqualTo("light rain")
    }
}