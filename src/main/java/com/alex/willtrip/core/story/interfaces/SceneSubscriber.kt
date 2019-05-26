package com.alex.willtrip.core.story.interfaces

import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

interface SceneSubscriber {
    fun addObserver (observer: DataObserver<Int>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}