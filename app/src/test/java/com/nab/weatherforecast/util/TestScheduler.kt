package com.nab.weatherforecast.util

import io.reactivex.schedulers.Schedulers

class TrampolineTestRxScheduler : RxScheduler() {
    private val testScheduler = Schedulers.trampoline()

    override fun androidMainThread() = testScheduler
    override fun computation() = testScheduler
    override fun io() = testScheduler
}