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

    fun resetToDefault (settingName: String) {
        defaulter.resetToDefault(settingName)
    }

    fun getSettingValue (settingName: String): Int {
        return accessor.getSetting(settingName)
    }

    fun setSettingValue (settingName: String, newValue: Int) {
        accessor.editSetting(settingName, newValue)
    }

    fun addSettingObserver (settingName:String, observer: DataObserver<Pair<String, Int>>): DataSubscription {
        return subscriber.addObserver(settingName, observer)
    }

    fun removeObserver(dataSubscription: DataSubscription) {
        subscriber.removeObserver(dataSubscription)
    }

}