package com.alex.willtrip.core.willpower.interfaces

import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

interface WPSubscriber {
    fun addObserver (observer: DataObserver<Int>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}