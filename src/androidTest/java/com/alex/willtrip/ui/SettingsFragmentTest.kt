package com.alex.willtrip.ui

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.common.truth.Truth.assertThat
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager
import com.alex.willtrip.di.DaggerAppComponent
import com.alex.willtrip.ui.OrientationChangeAction.Companion.orientationLandscape
import org.junit.Before
import org.junit.Test
import com.alex.willtrip.R


class SettingsFragmentTest {

    private lateinit var settingsManager: SettingsManager

    @Before
    fun prepareTest (){
        settingsManager = DaggerAppComponent.create().settingsManager()
        settingsManager.resetAllToDefault()

        rotateScreenToDefault()
    }

    @Test
    fun checkChainPointsSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.chainPointsSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.CHAIN_POINTS)).isEqualTo(0)

        onView(withId( R.id.chainPointsSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.CHAIN_POINTS)).isEqualTo(1)

        onView(withId( R.id.chainPointsSwitch)).perform(click())
        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.chainPointsSwitch)).check(matches(isNotChecked()))

        onView(isRoot()).perform(orientationLandscape())

        onView(withId(R.id.chainPointsSwitch)).check(matches(isNotChecked()))
    }

    @Test
    fun checkGratitudeSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.gratitudeSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.GRATITUDE)).isEqualTo(0)

        onView(withId( R.id.gratitudeSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.GRATITUDE)).isEqualTo(1)

        onView(withId( R.id.gratitudeSwitch)).perform(click())
        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.gratitudeSwitch)).check(matches(isNotChecked()))

        onView(isRoot()).perform(orientationLandscape())

        onView(withId(R.id.gratitudeSwitch)).check(matches(isNotChecked()))
    }

    private fun startApp() {
        val device = UiDevice.getInstance(getInstrumentation())
        val allAppsButton = device.findObject(
            UiSelector().description("Will Trip")
        )
        allAppsButton.clickAndWaitForNewWindow()
    }

    private fun rotateScreen() {
        val device = UiDevice.getInstance(getInstrumentation())
        device.setOrientationLeft()
    }


    private fun rotateScreenToDefault() {
        val device = UiDevice.getInstance(getInstrumentation())
        device.setOrientationNatural()
    }

}