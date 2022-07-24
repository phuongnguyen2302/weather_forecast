package com.nab.weatherforecast.util

import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class LazyTestRxScheduler : RxScheduler() {
    private val testScheduler: TestScheduler by lazy { TestScheduler() }

    override fun androidMainThread() = testScheduler
    override fun computation() = testScheduler
    override fun io() = testScheduler
}

class TrampolineTestRxScheduler : RxScheduler() {
    private val testScheduler = Schedulers.trampoline()

    override fun androidMainThread() = testScheduler
    override fun computation() = testScheduler
    override fun io() = testScheduler
}