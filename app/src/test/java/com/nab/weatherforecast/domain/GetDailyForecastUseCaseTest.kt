package com.nab.weatherforecast.domain

import com.nab.weatherforecast.data.DailyForecastRepositoryImpl
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.verify
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetDailyForecastUseCaseTest {

    private lateinit var sut: GetDailyForecastUseCase

    @Before
    fun setUp() {
        mockkObject(DailyForecastRepositoryImpl)
        sut = GetDailyForecastUseCase(DailyForecastRepositoryImpl)
    }

    @Test
    fun `execute - when calls execute then calls getDailyForecast via repository`() {
        // When
        sut.execute("location")

        // Then
        verify(exactly = 1) { DailyForecastRepositoryImpl.getDailyForecast("location") }
    }

    @Test
    fun `execute - when calls execute then returns expected daily forecast list`() {
        // Given
        val expectedList = listOf(
            DailyForecast(
                date = "Tue 11 Jul 2011",
                temperature = 10.0,
                pressure = 5,
                humidity = 6,
                weatherDescription = "rainy"
            ),
            DailyForecast(
                date = "Tue 11 Jul 2011",
                temperature = 10.0,
                pressure = 9,
                humidity = 19,
                weatherDescription = "sunny"
            )
        )
        every { DailyForecastRepositoryImpl.getDailyForecast("location") } returns Single.just(
            expectedList
        )

        // When
        val result = sut.execute("location").test()

        // Then
        result.assertValue(expectedList)
    }
}