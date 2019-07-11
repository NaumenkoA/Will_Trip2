package com.alex.willtrip.core.settings.interfaces

import com.alex.willtrip.core.settings.Setting
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

interface SettingSubscriber {
    fun addObserver (setting: Setting, observer: DataObserver<Pair<Setting,Int>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}