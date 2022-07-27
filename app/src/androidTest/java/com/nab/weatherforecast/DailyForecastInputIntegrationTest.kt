package com.nab.weatherforecast

import androidx.fragment.app.testing.launchFragmentInContainer
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.nab.weatherforecast.domain.DailyForecastRepository
import com.nab.weatherforecast.domain.DailyForecastResponse
import com.nab.weatherforecast.domain.GetDailyForecastUseCase
import com.nab.weatherforecast.ui.DailyForecastFragment
import com.nab.weatherforecast.ui.DailyForecastViewModel
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Test


class DailyForecastInputIntegrationTest : TestCase() {

    @MockK
    private lateinit var dailyForecastRepository: DailyForecastRepository

    private lateinit var sut: DailyForecastFragment

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = TestDailyForecastFragment(
            DailyForecastViewModel(
                GetDailyForecastUseCase(dailyForecastRepository),
                TrampolineTestRxScheduler()
            )
        )
    }

    @Test
    fun whenInputTextIsLessThan3CharactersThenErrorTextShouldBeShown() {
        // Given
        launchFragmentInContainer(themeResId = R.style.Theme_WeatherForecast) { sut }

        // When
        onScreen<DailyForecastSubScreen> {
            inputText.typeText("ab")
            getWeatherButton.click()

            // Then
            errorText.isCompletelyDisplayed()
            errorText.hasText("Location name must have 3 characters and above. Please try again!")
        }
    }

    @Test
    fun whenErrorOccursOnFetchingDataThenErrorTextIsShown() {
        // Given
        launchFragmentInContainer(themeResId = R.style.Theme_WeatherForecast) { sut }
        every { dailyForecastRepository.getDailyForecast("Saigon") } returns Single.just(
            DailyForecastResponse.Error("error message")
        )
        onScreen<DailyForecastSubScreen> {
            // When
            inputText.typeText("Saigon")
            getWeatherButton.click()

            // Then
            errorText.hasText("An error is occurred. Please check your connection and try again!")
        }
    }


    class TestDailyForecastFragment(
        viewModel: DailyForecastViewModel
    ) : DailyForecastFragment() {
        override var viewModel: DailyForecastViewModel? = viewModel
    }
}