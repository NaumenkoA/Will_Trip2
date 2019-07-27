package com.alex.willtrip.ui

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.scrollTo
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.common.truth.Truth.assertThat
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager
import com.alex.willtrip.di.DaggerAppComponent
import org.junit.Before
import org.junit.Test
import com.alex.willtrip.R
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import android.widget.TimePicker
import com.alex.willtrip.ui.actions.PickerActions
import org.hamcrest.Matchers

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
    }

    @Test
    fun checkSpecialDaysSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.sdSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.SPECIAL_DAYS)).isEqualTo(0)

        onView(withId( R.id.sdSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.SPECIAL_DAYS)).isEqualTo(1)

        onView(withId( R.id.sdSwitch)).perform(click())
        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.sdSwitch)).check(matches(isNotChecked()))
    }

    @Test
    fun checkNotificationSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())
        onView(withId(R.id.notifTimeTextView)).check(matches(isEnabled()))
        onView(withId(R.id.timeEditText)).check(matches(isEnabled()))


        onView(withId(R.id.notificationsSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.NOTIFICATIONS)).isEqualTo(0)
        onView(withId(R.id.notifTimeTextView)).check(matches(not(isEnabled())))
        onView(withId(R.id.timeEditText)).check(matches(not(isEnabled())))

        onView(withId( R.id.notificationsSwitch)).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.SPECIAL_DAYS)).isEqualTo(1)
        onView(withId(R.id.notifTimeTextView)).check(matches(isEnabled()))
        onView(withId(R.id.timeEditText)).check(matches(isEnabled()))

        onView(withId( R.id.notificationsSwitch)).perform(click())
        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.notificationsSwitch)).check(matches(isNotChecked()))
        onView(withId(R.id.notifTimeTextView)).check(matches(not(isEnabled())))
        onView(withId(R.id.timeEditText)).check(matches(not(isEnabled())))
    }

    @Test
    fun checkEveryNDaysStrictSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.everyNDaysStrictSwitch)).perform(scrollTo(), click())
        assertThat(settingsManager.getSettingValue(Setting.EVERY_N_DAYS_STRICT)).isEqualTo(1)

        onView(withId( R.id.everyNDaysStrictSwitch)).perform(scrollTo(), click())
        assertThat(settingsManager.getSettingValue(Setting.EVERY_N_DAYS_STRICT)).isEqualTo(0)

        onView(withId( R.id.everyNDaysStrictSwitch)).perform(scrollTo(), click())
        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.everyNDaysStrictSwitch)).check(matches(isChecked()))
    }

    @Test
    fun checkDelayedDaysSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.delayDaysSpinner)).perform(click())
        onView(withText("4")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.DELAYED_DAYS)).isEqualTo(4)

        onView(withId(R.id.delayDaysSpinner)).perform(click())
        onView(withText("1")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.DELAYED_DAYS)).isEqualTo(1)

        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.delayDaysSpinner)).check(matches(withSpinnerText(containsString("1"))))
    }

    @Test
    fun checkWeekStartsFromSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.weekStartsFromSpinner)).perform(scrollTo(), click())
        onView(withText("Sun")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.WEEK_STARTS_MON)).isEqualTo(0)

        onView(withId(R.id.weekStartsFromSpinner)).perform(click())
        onView(withText("Mon")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.WEEK_STARTS_MON)).isEqualTo(1)

        onView(withId(R.id.weekStartsFromSpinner)).perform(click())
        onView(withText("Sun")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.WEEK_STARTS_MON)).isEqualTo(0)

        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.weekStartsFromSpinner)).check(matches(withSpinnerText(containsString("Sun"))))
    }

    @Test
    fun checkNotificationTimeSetting() {

        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.timeEditText)).perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name))).perform(PickerActions.setTime (16, 30))
        onView(withText("OK")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.NOTIFICATION_TIME)).isEqualTo((16*3600 + 30*60))
        onView(withId(R.id.timeEditText)).check(matches(withText("16:30")))

        onView(withId(R.id.timeEditText)).perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name))).perform(PickerActions.setTime (8, 9))
        onView(withText("OK")).perform(click())
        assertThat(settingsManager.getSettingValue(Setting.NOTIFICATION_TIME)).isEqualTo((8*3600 + 9*60))
        onView(withId(R.id.timeEditText)).check(matches(withText("08:09")))

        pressBackUnconditionally()

        rotateScreen()
        startApp()

        onView(withId( R.id.action_settings)).perform(click())
        onView(withId(R.id.timeEditText)).check(matches(withText("08:09")))
    }

    @Test
    fun checkSettingsToDefault() {
        startApp()

        onView(withId(R.id.action_settings)).perform(click())

        onView(withId(R.id.chainPointsSwitch)).perform(click())
        onView(withId(R.id.gratitudeSwitch)).perform(click())
        onView(withId(R.id.sdSwitch)).perform(click())
        onView(withId(R.id.delayDaysSpinner)).perform(click())
        onView(withText("4")).perform(click())

        onView(withId(R.id.timeEditText)).perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name))).perform(PickerActions.setTime (16, 30))
        onView(withText("OK")).perform(click())
        onView(withId(R.id.notificationsSwitch)).perform(click())

        onView(withId(R.id.everyNDaysStrictSwitch)).perform(scrollTo(), click())
        onView(withId(R.id.weekStartsFromSpinner)).perform(scrollTo(), click())
        onView(withText("Sun")).perform(click())

        onView((withId(R.id.toDefaultTextView))).perform(scrollTo(),click())
        onView(withText("YES")).perform(click())

        Setting.values().forEach {
            assertThat(settingsManager.getSettingValue(it)).isEqualTo(it.default)
            when (it){
                Setting.CHAIN_POINTS -> onView (withId(R.id.chainPointsSwitch)).check(matches(isChecked()))
                Setting.GRATITUDE -> onView (withId(R.id.gratitudeSwitch)).check(matches(isChecked()))
                Setting.SPECIAL_DAYS -> onView (withId(R.id.sdSwitch)).check(matches(isChecked()))
                Setting.NOTIFICATIONS -> onView (withId(R.id.notificationsSwitch)).check(matches(isChecked()))
                Setting.EVERY_N_DAYS_STRICT -> onView (withId(R.id.everyNDaysStrictSwitch)).check(matches(isNotChecked()))
                Setting.WEEK_STARTS_MON -> onView (withId(R.id.weekStartsFromSpinner)).check(matches(withSpinnerText(containsString("Mon"))))
                Setting.DELAYED_DAYS -> onView (withId(R.id.delayDaysSpinner)).check(matches(withSpinnerText(containsString("3"))))
                Setting.NOTIFICATION_TIME -> {
                    onView (withId(R.id.timeEditText)).check(matches(withText(containsString("22:00"))))
                    onView (withId(R.id.timeEditText)).check(matches(isEnabled()))
                }
            }
        }
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

