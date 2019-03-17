package com.alex.willtrip.settings

import com.alex.willtrip.settings.interfaces.SettingAccessor
import com.alex.willtrip.settings.interfaces.SettingsDB
import java.lang.IllegalArgumentException

class SettingAccessorImp (val settingsDB: SettingsDB): SettingAccessor {
    override fun editSetting(settingName: String, newValue: Int) {
        val setting = Setting.valueOf(settingName)
        if (newValue > setting.maxValue) throw
        IllegalArgumentException ("${SettingAccessorImp::class.java.simpleName} passed setting ${setting.name} value is more than it's maximum value")
        settingsDB.saveSetting(setting.name, newValue)
    }

    override fun getSetting(settingName: String): Int {
        val setting = Setting.valueOf(settingName)
        return settingsDB.readSettingOrDefault(setting.name)
    }
}