package com.alex.willtrip.core.settings

import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.di.DaggerAppComponent
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.SettingEntity
import org.junit.Test

import com.google.common.truth.Truth.assertThat
import io.objectbox.reactive.DataObserver
import org.junit.Before
import java.lang.IllegalArgumentException

class SettingsManagerTest: AbstractObjectBoxTest() {

    lateinit var settingsManager: SettingsManager

    @Before
    fun setUp() {
        settingsManager = DaggerAppComponent.builder().build().settingsManager()
    }


    @Test (expected = IllegalArgumentException::class)
    fun wrongValueThrowsException() {
        settingsManager.setSettingValue(Setting.CHAIN_POINTS, 2)
    }

    @Test
    fun checkDefaultWhenNoValueSet() {
        ObjectBox.boxStore.boxFor(SettingEntity::class.java).all.clear()
        val default = settingsManager.getSettingValue(Setting.NOTIFICATION_TIME)

        assertThat(default).isEqualTo(79200)
    }

    @Test
    fun resetAllToDefault() {
        settingsManager.setSettingValue(Setting.NOTIFICATION_TIME, 1000)
        settingsManager.setSettingValue(Setting.CHAIN_POINTS, 0)


        settingsManager.resetAllToDefault()
        val notifTimeSetting = settingsManager.getSettingValue(Setting.NOTIFICATION_TIME)
        val chainSetting = settingsManager.getSettingValue(Setting.CHAIN_POINTS)

        assertThat(notifTimeSetting).isEqualTo(79200)
        assertThat(chainSetting).isEqualTo(1)
    }

    @Test
    fun resetToDefault() {
        settingsManager.setSettingValue(Setting.NOTIFICATION_TIME, 1000)
        settingsManager.resetToDefault(Setting.NOTIFICATION_TIME)

        val notifTimeSetting = settingsManager.getSettingValue(Setting.NOTIFICATION_TIME)

        assertThat(notifTimeSetting).isEqualTo(79200)
    }

    @Test
    fun setAndGetSettingValue() {
        settingsManager.setSettingValue(Setting.DELAYED_DAYS, 2)
        settingsManager.setSettingValue(Setting.DELAYED_DAYS, 1)

        val loaded = settingsManager.getSettingValue(Setting.DELAYED_DAYS)

        assertThat(loaded).isEqualTo(1)
    }

    @Test
    fun checkSettingObserver() {
        val observer = SettingChangeObserver()
        val subscriber = settingsManager.addSettingObserver(Setting.NOTIFICATION_TIME, observer)
        settingsManager.setSettingValue(Setting.NOTIFICATION_TIME, 10000)

        Thread.sleep(500)

        settingsManager.removeObserver(subscriber)
        settingsManager.setSettingValue(Setting.NOTIFICATION_TIME, 5000)

        assertThat (observer.settingName).isEqualTo(Setting.NOTIFICATION_TIME.name)
        assertThat (observer.settingValue).isEqualTo(10000)
    }

    class SettingChangeObserver: DataObserver<Pair<Setting, Int>> {

        var settingName = ""
        var settingValue: Int = 0

        override fun onData(data: Pair<Setting, Int>) {
            settingName = data.first.name
            settingValue = data.second
        }
    }
}