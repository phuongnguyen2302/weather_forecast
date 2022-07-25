package com.nab.weatherforecast

import androidx.fragment.app.testing.launchFragmentInContainer
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.nab.weatherforecast.domain.DailyForecast
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
import org.junit.After
import org.junit.Before
import org.junit.Test


class DailyForecastIntegrationTest : TestCase() {

    @MockK
    private lateinit var dailyForecastRepository: DailyForecastRepository

    private lateinit var sut: DailyForecastFragment

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = TestDailyForecastFragment(
            DailyForecastViewModel(
                GetDailyForecastUseCase(dailyForecastRepository), TrampolineTestRxScheduler()
            )
        )
    }

    @After
    fun tearDown() {
    }


    @Test
    fun whenDailyForecastListIsReceivedThenShowRecycleView() {
        // Given
        launchFragmentInContainer(themeResId = R.style.Theme_WeatherForecast) { sut }
        every { dailyForecastRepository.getDailyForecast("Saigon") } returns Single.just(
            DailyForecastResponse.Success(
                listOf(
                    DailyForecast(
                        date = "Wed, 26 Jul 2022",
                        temperature = 25.0,
                        pressure = 1029,
                        humidity = 50,
                        weatherDescription = "sunny"
                    ),
                    DailyForecast(
                        date = "Wed, 26 Jul 2022",
                        temperature = 25.0,
                        pressure = 1029,
                        humidity = 50,
                        weatherDescription = "sunny"
                    ),
                    DailyForecast(
                        date = "Wed, 26 Jul 2022",
                        temperature = 25.0,
                        pressure = 1029,
                        humidity = 50,
                        weatherDescription = "sunny"
                    ),
                    DailyForecast(
                        date = "Wed, 26 Jul 2022",
                        temperature = 25.0,
                        pressure = 1029,
                        humidity = 50,
                        weatherDescription = "sunny"
                    ),
                )
            )
        )
        onScreen<DailyForecastSubScreen> {
            // When
            inputText.typeText("Saigon")
            getWeatherButton.click()

            // Then
            recyclerView {
                isDisplayed()
                (0..3).forEach { _ ->
                    isCompletelyDisplayed()
                }
            }
        }
    }

    @Test
    fun whenDailyForecastListIsReceivedThenShowExpectedData() {
        // Given
        launchFragmentInContainer(themeResId = R.style.Theme_WeatherForecast) { sut }
        every { dailyForecastRepository.getDailyForecast("Saigon") } returns Single.just(
            DailyForecastResponse.Success(
                listOf(
                    DailyForecast(
                        date = "Tue, 25 Jul 2022",
                        temperature = 20.0,
                        pressure = 1031,
                        humidity = 71,
                        weatherDescription = "light rain"
                    )
                )
            )
        )
        onScreen<DailyForecastSubScreen> {
            // When
            inputText.typeText("Saigon")
            getWeatherButton.click()

            // Then
            recyclerView {
                isDisplayed()
                childAt<DailyForecastItem>(0) {
                    date.hasText("Tue, 25 Jul 2022")
                    averageTemperature.hasText("20.0˚C")
                    pressure.hasText("1031")
                    humidity.hasText("71%")
                    description.hasText("light rain")                }
            }
        }
    }


    class TestDailyForecastFragment(
        viewModel: DailyForecastViewModel
    ) : DailyForecastFragment() {
        override var viewModel: DailyForecastViewModel? = viewModel
    }
}