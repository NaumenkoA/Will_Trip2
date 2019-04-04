package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.objectbox.class_boxes.DoDB
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

interface DoSubscriber {
    fun addObserver (observer: DataObserver<Class<DoDB>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}