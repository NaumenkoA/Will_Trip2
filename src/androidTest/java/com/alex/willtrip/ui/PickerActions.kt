package com.alex.willtrip.ui

import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.allOf
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import org.hamcrest.Matcher

/**
 * Espresso action for interacting with [DatePicker] and
 * [TimePicker].
 *
 * @see [Pickers
 * API guide](http://developer.android.com/guide/topics/ui/controls/pickers.html)
 */
object PickerActions {
    /**
     * Returns a [ViewAction] that sets a date on a [DatePicker].
     */
    fun setDate(year: Int, monthOfYear: Int, dayOfMonth: Int): ViewAction {
        // monthOfYear which starts with zero in DatePicker widget.
        val normalizedMonthOfYear = monthOfYear - 1
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                val datePicker = view as DatePicker
                datePicker.updateDate(year, normalizedMonthOfYear, dayOfMonth)
            }

            override fun getDescription(): String {
                return "set date"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isAssignableFrom(DatePicker::class.java), isDisplayed())
            }
        }
    }

    /**
     * Returns a [ViewAction] that sets a time on a [TimePicker].
     */
    fun setTime(hours: Int, minutes: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController, view: View) {
                val timePicker = view as TimePicker
                timePicker.currentHour = hours
                timePicker.currentMinute = minutes
            }

            override fun getDescription(): String {
                return "set time"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(isAssignableFrom(TimePicker::class.java), isDisplayed())
            }
        }
    }
}// no Instance