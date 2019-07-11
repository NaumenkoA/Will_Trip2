package com.alex.willtrip.core.settings.interfaces

interface SettingToDefault {
  fun resetToDefault (settingName: String)
  fun resetAllToDefault()
}
       