package com.alex.willtrip.settings.interfaces

interface SettingToDefault {
    fun resetToDefault (settingName: String)
    fun resetAllToDefault()
}