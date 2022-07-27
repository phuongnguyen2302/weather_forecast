package com.nab.weatherforecast.data

import android.accounts.NetworkErrorException
import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.DailyForecastResponse
import com.nab.weatherforecast.util.TrampolineTestRxScheduler
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class DailyForecastRepositoryImplTest {

    @MockK
    private lateinit var api: WeatherForecastAPI

    private lateinit var sut: DailyForecastRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = DailyForecastRepositoryImpl(TrampolineTestRxScheduler())

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
    fun `getDailyForecast - when response is failed then returns error with exception`() {
        // Given
        val errorBody = "".toResponseBody("application/json".toMediaType())
        val errorResponse: Response<DailyForecastListDto> = Response.error(400, errorBody)
        every { api.getDailyForecast("location") } returns Single.just(errorResponse)

        // When
        val result = sut.getDailyForecast("location").test()

        // Then
        result.assertValue(DailyForecastResponse.Error(errorResponse.message()))
    }

    @Test
    fun `getDailyForecast - when response body is null then returns error with exception`() {
        // Given
        val successResponse: Response<DailyForecastListDto> = Response.success(null)
        every { api.getDailyForecast("location") } returns Single.just(successResponse)

        // When
        val result = sut.getDailyForecast("location").test()

        // Then
        result.assertValue(DailyForecastResponse.Error(successResponse.message()))
    }

    @Test
    fun `getDailyForecast - when error occurs on api call is null then returns error with exception`() {
        // Given
        val exception = NetworkErrorException("no connection")
        every { api.getDailyForecast("location") } returns Single.error(exception)

        // When
        val result = sut.getDailyForecast("location").test()

        // Then
        result.assertValue(
            DailyForecastResponse.Error(
                exception.message ?: "Network or parsing error", exception
            )
        )
    }

    @Test
    fun `getDailyForecast - when response is succeed then returns expected list`() {
        // Given
        val firstItem = DailyForecastDto(
            date = 123,
            temperature = DailyForecastTemperatureDto(10.0),
            pressure = 5,
            humidity = 6,
            weatherDescription = listOf(DailyForecastDescriptionDto("rainy"))
        )
        val secondItem = DailyForecastDto(
            date = 167,
            temperature = DailyForecastTemperatureDto(10.0),
            pressure = 9,
            humidity = 19,
            weatherDescription = listOf(DailyForecastDescriptionDto("sunny"))
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
        every { DailyForecastMapper.map(firstItem) } returns mappedFirstItem
        every { DailyForecastMapper.map(secondItem) } returns mappedSecondItem
        every { api.getDailyForecast("location") } returns Single.just(
            Response.success(DailyForecastListDto(listOf(firstItem, secondItem)))
        )
        // When
        val result = sut.getDailyForecast("location").test()

        // Then
        result.assertValue(DailyForecastResponse.Success(listOf(mappedFirstItem, mappedSecondItem)))
    }
}