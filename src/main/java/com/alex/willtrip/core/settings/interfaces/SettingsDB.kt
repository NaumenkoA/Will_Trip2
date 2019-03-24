package com.alex.willtrip.core.settings.interfaces

interface SettingsDB {
    fun saveSetting (settingName: String, settingValue: Int)
    fun readSettingOrDefault (settingName: String): Int
}