package com.nab.weatherforecast.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.DailyForecastInputException
import com.nab.weatherforecast.domain.DailyForecastNetworkException
import com.nab.weatherforecast.domain.GetDailyForecastUseCase
import com.nab.weatherforecast.util.TrampolineTestRxScheduler
import io.mockk.*
import io.mockk.impl.annotations.MockK
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
        sut = spyk(DailyForecastViewModel(getDailyForecastUseCase, TrampolineTestRxScheduler()))
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
        val exception = spyk(DailyForecastNetworkException("some exception"))
        every { getDailyForecastUseCase.execute(any()) } returns Single.error(exception)

        // When
        sut.dailyForecastList.observeForever(dailyForecastListObserver)
        sut.onGetWeatherClick()

        // Then
        verify(exactly = 1) { exception.printStackTrace() }
        assertThat(sut.errorText.get()).isEqualTo("An error is occurred. Please check your connection and try again!")
    }

    @Test
    fun `onGetWeatherClick - when input error is returned then set error text`() {
        // Given
        val exception = spyk(DailyForecastInputException)
        every { getDailyForecastUseCase.execute(any()) } returns Single.error(exception)

        // When
        sut.dailyForecastList.observeForever(dailyForecastListObserver)
        sut.onGetWeatherClick()

        // Then
        verify(exactly = 1) { exception.printStackTrace() }
        assertThat(sut.errorText.get()).isEqualTo("Location name must have 3 characters and above. Please try again!")
    }

    @Test
    fun `onGetWeatherClick - then error text set to empty()`() {
        // Given
        every { getDailyForecastUseCase.execute(any()) } returns Single.just(listOf(
            DailyForecast(
                date = "Thu, 01 Jan 1970",
                temperature = 10.0,
                pressure = 6,
                humidity = 10,
                weatherDescription = "rainy"
            )))
        sut.errorText.set("location")

        // When
        sut.onGetWeatherClick()

        // Then
        assertThat(sut.errorText.get()).isEqualTo("")
    }

    @Test
    fun `onGetWeatherClick - then list is cleared()`() {
        // Given
        every { getDailyForecastUseCase.execute(any()) } returns Single.error(DailyForecastNetworkException(""))
        sut.dailyForecastList.observeForever(dailyForecastListObserver)

        // When
        sut.onGetWeatherClick()

        // Then
        verify(exactly = 2) { dailyForecastListObserver.onChanged(emptyList()) }
    }

    @Test
    fun `onTextChanged - then error text set to empty()`() {
        // Given
        every { getDailyForecastUseCase.execute(any()) } returns Single.just(listOf(
            DailyForecast(
                date = "Thu, 01 Jan 1970",
                temperature = 10.0,
                pressure = 6,
                humidity = 10,
                weatherDescription = "rainy"
            )))
        sut.errorText.set("location")

        // When
        sut.onTextChanged("av", 1, 2, 3)

        // Then
        assertThat(sut.errorText.get()).isEqualTo("")
    }

    @Test
    fun `onTextChanged - then list is cleared()`() {
        // Given
        every { getDailyForecastUseCase.execute(any()) } returns Single.just(mockk())
        sut.dailyForecastList.observeForever(dailyForecastListObserver)

        // When
        sut.onTextChanged("av", 1, 2, 3)

        // Then
        verify(exactly = 2) { dailyForecastListObserver.onChanged(emptyList()) }
    }
}
