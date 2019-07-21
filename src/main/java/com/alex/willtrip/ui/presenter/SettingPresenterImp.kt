package com.alex.willtrip.ui.presenter

import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager

class SettingPresenterImp(val settingsManager: SettingsManager):SettingPresenter {

    override fun getAllSettingsValue(): List<Pair<Setting, Int>> {
        val list = mutableListOf<Pair<Setting, Int>>()
        Setting.values().forEach {
            val value = settingsManager.getSettingValue(it)
            list.add (Pair(it, value))
        }
        return list
    }

    override fun resetAllToDefault() {
        settingsManager.resetAllToDefault()
    }

    override fun onSettingChanged(setting: Setting, newValue: Int) {
        settingsManager.setSettingValue(setting, newValue)
    }
}