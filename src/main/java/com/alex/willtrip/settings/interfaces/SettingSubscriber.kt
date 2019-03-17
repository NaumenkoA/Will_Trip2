package com.alex.willtrip.settings.interfaces

import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

interface SettingSubscriber {
    fun addObserver (settingName: String, observer: DataObserver<Pair<String,Int>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}