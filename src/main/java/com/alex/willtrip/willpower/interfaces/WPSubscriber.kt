package com.alex.willtrip.willpower.interfaces

import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

interface WPSubscriber {
    fun addObserver (observer: DataObserver<Int>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}