package com.nab.weatherforecast.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.GetDailyForecastUseCase
import com.nab.weatherforecast.util.TrampolineTestRxScheduler
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DailyForecastViewModelTest {
    @Rule
    @JvmField
    var executorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getDailyForecastUseCase: GetDailyForecastUseCase

    @MockK(relaxUnitFun = true)
    private lateinit var dailyForecastListObserver: Observer<List<DailyForecastItemViewModel>>

    private lateinit var sut: DailyForecastViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = DailyForecastViewModel(getDailyForecastUseCase, TrampolineTestRxScheduler())
    }

    @Test
    fun `onPause - then no subscription is triggered`() {
        // When
        sut.onPause()

        // Then
        assertThat(sut.disposable.size()).isEqualTo(0)
    }

    @Test
    fun `onGetWeatherClick - then subscription is added to the disposable`() {
        // Given
        every { getDailyForecastUseCase.execute(any()) } returns Single.just(emptyList())

        // When
        sut.onGetWeatherClick()

        // Then
        assertThat(sut.disposable.size()).isEqualTo(1)
    }

    @Test
    fun `onGetWeatherClick - when forecast list is returned then dailyForecastList is set`() {
        // Given
        val expectedList = listOf(
            DailyForecast(
                date = "Thu, 01 Jan 1970",
                temperature = 10.0,
                pressure = 6,
                humidity = 10,
                weatherDescription = "rainy"
            ),
            DailyForecast(
                date = "Wed, 02 Jul 2020",
                temperature = 15.0,
                pressure = 60,
                humidity = 4,
                weatherDescription = "sunny"
            )
        )
        val expectedItemViewModelList = listOf(
            DailyForecastItemViewModel(
                date = "Thu, 01 Jan 1970",
                temperature = "10.0˚C",
                pressure = "6",
                humidity = "10%",
                weatherDescription = "rainy"
            ),
            DailyForecastItemViewModel(
                date = "Wed, 02 Jul 2020",
                temperature = "15.0˚C",
                pressure = "60",
                humidity = "4%",
                weatherDescription = "sunny"
            )
        )
        every { getDailyForecastUseCase.execute(any()) } returns Single.just(expectedList)

        // When
        sut.dailyForecastList.observeForever(dailyForecastListObserver)
        sut.onGetWeatherClick()

        // Then
        verify(exactly = 1) { dailyForecastListObserver.onChanged(expectedItemViewModelList) }
    }

    @Test
    fun `onGetWeatherClick - then call execute with current input text`() {
        // Given
        sut.onTextChanged("some location", 1, 1, 1)
        every { getDailyForecastUseCase.execute(any()) } returns Single.just(emptyList())

        // When
        sut.dailyForecastList.observeForever(dailyForecastListObserver)
        sut.onGetWeatherClick()

        // Then
        verify(exactly = 1) { getDailyForecastUseCase.execute("some location") }
    }

    @Test
    fun `onGetWeatherClick - when error is returned then set error text`() {
        // Given
        val exception = spyk(Exception("some exception"))
        every { getDailyForecastUseCase.execute(any()) } returns Single.error(exception)

        // When
        sut.dailyForecastList.observeForever(dailyForecastListObserver)
        sut.onGetWeatherClick()

        // Then
        verify(exactly = 1) { exception.printStackTrace() }
        assertThat(sut.errorText.value).isEqualTo("An error is occurred. Please try again later!")
    }
}
