package com.nab.weatherforecast

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.nab.weatherforecast.domain.DailyForecast
import com.nab.weatherforecast.domain.DailyForecastRepository
import com.nab.weatherforecast.domain.DailyForecastResponse
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DailyForecastIntegrationTest : TestCase() {

    @Rule
    @JvmField
    var rule: ActivityScenarioRule<*> = ActivityScenarioRule(MainActivity::class.java)

    @MockK
    private lateinit var dailyForecastRepository: DailyForecastRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        rule.scenario.onActivity {
            val application = (it.application as DailyForecastApplication)
            application.dailyForecastRepository = dailyForecastRepository
        }
    }

    @After
    fun tearDown() {
    }


    @Test
    fun whenDailyForecastListIsReceivedThenShowRecycleView() {
        // Given
        every { dailyForecastRepository.getDailyForecast("Saigon") } returns Single.just(
            DailyForecastResponse.Success(
                listOf(
                    DailyForecast(
                        date = "Tue, 25 Jul 2022",
                        temperature = 20.0,
                        pressure = 1031,
                        humidity = 71,
                        weatherDescription = "light rain"
                    ),
                    DailyForecast(
                        date = "Wed, 26 Jul 2022",
                        temperature = 25.0,
                        pressure = 1029,
                        humidity = 50,
                        weatherDescription = "sunny"
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
                    averageTemperature.hasText("20˚C")
                    pressure.hasText("1031")
                    humidity.hasText("71%")
                    description.hasText("light rain")
                }
                childAt<DailyForecastItem>(1) {
                    date.hasText("Wed, 26 Jul 2022")
                    averageTemperature.hasText("25˚C")
                    pressure.hasText("1029")
                    humidity.hasText("50%")
                    description.hasText("sunny")
                }
            }
        }
    }
}