package com.alex.willtrip.willpower

import com.alex.willtrip.willpower.interfaces.WPChangeSubscriber
import com.alex.willtrip.willpower.interfaces.WPLoader
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class WPManager (val mutator: Mutator, val loader: WPLoader, val subscriber: WPChangeSubscriber) {

    init {
        mutator.wp = loadWP()
    }

    private fun loadWP ()= loader.loadWillPower()

    fun getCurrentWP (): Int = mutator.getCurrentWP()

    fun increaseWP (value: Int): Int {
        val newWP = mutator.increase(value)
        saveWP(newWP)
        return newWP
    }

    private fun saveWP (value: Int) {
        val willPower = WillPower()
        willPower.willPower = value
        loader.saveWillPower(willPower)
    }

    fun decreaseWP (value: Int): Int {
        val newWP = mutator.decrease(value)
        saveWP(newWP)
        return newWP
    }

    fun addWPObserver (dataObserver: DataObserver <Int>): DataSubscription {
        return subscriber.addObserver(dataObserver)
    }

    fun removeWPObserver (dataSubscription: DataSubscription) {
        subscriber.removeObserver(dataSubscription)
    }
}