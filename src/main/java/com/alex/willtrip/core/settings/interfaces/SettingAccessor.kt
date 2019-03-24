package com.alex.willtrip.core.settings.interfaces

interface SettingAccessor {
    fun editSetting(settingName: String, newValue: Int)
    fun getSetting (settingName: String): Int
}