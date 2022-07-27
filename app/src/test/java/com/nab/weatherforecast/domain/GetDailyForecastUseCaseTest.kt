package com.nab.weatherforecast.domain

import com.nab.weatherforecast.data.DailyForecastRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetDailyForecastUseCaseTest {

    private lateinit var sut: GetDailyForecastUseCase

    @MockK
    private lateinit var dailyForecastRepositoryImpl: DailyForecastRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = GetDailyForecastUseCase(dailyForecastRepositoryImpl)
    }

    @Test
    fun `execute - when input has less than 3 characters then throws InputException`() {
        // Given
        // When
        val result = sut.execute("ab").test()

        // Then
        result.assertError(DailyForecastInputException::class.java)
    }

    @Test
    fun `execute - when input has less than 3 characters then no repo call`() {
        // Given
        // When
        val result = sut.execute("ab").test()

        // Then
        verify(exactly = 0) { dailyForecastRepositoryImpl.getDailyForecast(any()) }
    }

    @Test
    fun `execute - when calls execute then calls getDailyForecast via repository`() {
        // Given
        every { dailyForecastRepositoryImpl.getDailyForecast("location") } returns
                Single.just(DailyForecastResponse.Success(mockk()))

        // When
        sut.execute("location")

        // Then
        verify(exactly = 1) { dailyForecastRepositoryImpl.getDailyForecast("location") }
    }

    @Test
    fun `execute - when returns error response then should throw exception`() {
        // Given
        val message = "exceptionMessage"
        every { dailyForecastRepositoryImpl.getDailyForecast("location") } returns
                Single.just(DailyForecastResponse.Error(message))

        // When
        val result = sut.execute("location").test()

        // Then
        result.assertError(DailyForecastException::class.java)
        result.assertErrorMessage(message)
    }


    @Test
    fun `execute - when returns empty response then should throw exception`() {
        // Given
        val message = "exceptionMessage"
        every { dailyForecastRepositoryImpl.getDailyForecast("location") } returns
                Single.just(DailyForecastResponse.Error(message))

        // When
        val result = sut.execute("location").test()

        // Then
        result.assertError(DailyForecastException::class.java)
        result.assertErrorMessage(message)
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
        every { dailyForecastRepositoryImpl.getDailyForecast("location") } returns
                Single.just(DailyForecastResponse.Success(expectedList))

        // When
        val result = sut.execute("location").test()

        // Then
        result.assertValue(expectedList)
    }
}