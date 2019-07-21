package com.alex.willtrip.ui.presenter

import com.alex.willtrip.core.settings.Setting

interface SettingPresenter {
    fun getAllSettingsValue(): List <Pair<Setting, Int>>
    fun onSettingChanged(setting: Setting, newValue: Int)
    fun resetAllToDefault()
}