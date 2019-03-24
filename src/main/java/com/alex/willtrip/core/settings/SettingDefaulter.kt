package com.alex.willtrip.core.settings

import com.alex.willtrip.core.settings.interfaces.SettingToDefault
import com.alex.willtrip.core.settings.interfaces.SettingsDB

class SettingDefaulter (val settingDB: SettingsDB): SettingToDefault {

    override fun resetAllToDefault() {
       enumValues<Setting>().forEach {
           resetToDefault(it.name)
       }
    }

    override fun resetToDefault(settingName: String) {
        val setting = Setting.valueOf(settingName)
        settingDB.saveSetting(setting.name, setting.default)
    }


}