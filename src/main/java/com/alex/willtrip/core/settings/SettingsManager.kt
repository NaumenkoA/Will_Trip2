package com.alex.willtrip.core.settings

import com.alex.willtrip.core.settings.interfaces.SettingAccessor
import com.alex.willtrip.core.settings.interfaces.SettingSubscriber
import com.alex.willtrip.core.settings.interfaces.SettingToDefault
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class SettingsManager (private val accessor: SettingAccessor, private val subscriber: SettingSubscriber, private val defaulter: SettingToDefault) {

    fun resetAllToDefault () {
        defaulter.resetAllToDefault()
    }

    fun resetToDefault (setting: Setting) {
        defaulter.resetToDefault(setting.name)
    }

    fun getSettingValue (setting: Setting): Int {
        return accessor.getSetting(setting.name)
    }

    fun setSettingValue (setting: Setting, newValue: Int) {
        accessor.editSetting(setting.name, newValue)
    }

    fun addSettingObserver (setting: Setting, observer: DataObserver<Pair<Setting, Int>>): DataSubscription {
        return subscriber.addObserver(setting, observer)
    }

    fun removeObserver(dataSubscription: DataSubscription) {
        subscriber.removeObserver(dataSubscription)
    }

}