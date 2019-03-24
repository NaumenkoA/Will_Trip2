package com.alex.willtrip.core.willpower

import com.alex.willtrip.core.willpower.interfaces.WPSubscriber
import com.alex.willtrip.core.willpower.interfaces.WPLoader
import com.alex.willtrip.core.willpower.interfaces.WPMutator
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class WPManager (private val mutator: WPMutator, private val loader: WPLoader, private val subscriber: WPSubscriber) {

    init {
        val willPower = loadWP() ?: WillPower()
        mutator.setWP(willPower)
    }

    private fun loadWP ()= loader.loadWillPower()

    fun getCurrentWP (): Int {
        return loader.loadWillPower()?.willPower ?: 0
    }

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