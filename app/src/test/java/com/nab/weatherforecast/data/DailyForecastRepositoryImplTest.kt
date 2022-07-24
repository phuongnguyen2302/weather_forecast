package com.nab.weatherforecast.data

import com.nab.weatherforecast.domain.DailyForecast
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

class DailyForecastRepositoryImplTest {

    @MockK
    private lateinit var api: WeatherForecastAPI

    private lateinit var sut: DailyForecastRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = DailyForecastRepositoryImpl

        mockkObject(BackendRetrofitBuilder)
        mockkObject(DailyForecastMapper)

        every { BackendRetrofitBuilder.weatherApi() } returns api
        every { api.getDailyForecast(any()) } returns Single.just(mockk())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getDailyForecast - when invoke then call getDailyForecastApi`() {
        // When
        sut.getDailyForecast("location")

        // Then
        verify(exactly = 1) { api.getDailyForecast("location") }
    }

    @Test
    fun `getDailyForecast - when invokes then call returns expected value`() {
        // Given
        val firstItem = DailyForecastDto(
            date = 123,
            temperature = DailyForecastTemperatureDto(10.0),
            pressure = 5,
            humidity = 6,
            weatherDescription = DailyForecastDescriptionDto("rainy")
        )
        val secondItem = DailyForecastDto(
            date = 167,
            temperature = DailyForecastTemperatureDto(10.0),
            pressure = 9,
            humidity = 19,
            weatherDescription = DailyForecastDescriptionDto("sunny")
        )
        val mappedFirstItem = DailyForecast(
            date = "Tue 11 Jul 2011",
            temperature = 10.0,
            pressure = 5,
            humidity = 6,
            weatherDescription = "rainy"
        )

        val mappedSecondItem = DailyForecast(
            date = "Tue 11 Jul 2011",
            temperature = 10.0,
            pressure = 9,
            humidity = 19,
            weatherDescription = "sunny"
        )

        every { api.getDailyForecast("location") } returns Single.just(
            DailyForecastListDto(
                dailyList = listOf(firstItem, secondItem)
            )
        )
        every { DailyForecastMapper.map(firstItem) } returns mappedFirstItem
        every { DailyForecastMapper.map(secondItem) } returns mappedSecondItem

        // When
        val result = sut.getDailyForecast("location").test()

        // Then
        result.assertValue(listOf(mappedFirstItem, mappedSecondItem))
    }
}