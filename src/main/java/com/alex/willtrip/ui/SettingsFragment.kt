package com.alex.willtrip.ui


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

import com.alex.willtrip.R
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.di.DaggerAppComponent
import com.alex.willtrip.extensions.toBoolean
import com.alex.willtrip.extensions.toInt
import com.alex.willtrip.ui.presenter.SettingPresenter
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var settingPresenter: SettingPresenter

    var delayDaysInteractions = 0

    var weekStartsFromInteractions = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateSpinner(weekStartsFromSpinner, R.array.array_week_starts_from)
        populateSpinner(delayDaysSpinner, R.array.array_delay_days)

        timeEditText.inputType = InputType.TYPE_NULL;
        timeEditText.requestFocus()

        timeEditText.setOnClickListener {
            val hour = timeEditText.text.split(":")[0]
            val minute = timeEditText.text.split(":")[1]

            showTimePickerDialog(hour.toInt(), minute.toInt())
        }

        chainPointsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            settingPresenter.onSettingChanged(
                Setting.CHAIN_POINTS,
                isChecked.toInt()
            )
        }

        sdSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            settingPresenter.onSettingChanged(
                Setting.SPECIAL_DAYS,
                isChecked.toInt()
            )
        }

        gratitudeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            settingPresenter.onSettingChanged(
                Setting.GRATITUDE,
                isChecked.toInt()
            )
        }

        notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            settingPresenter.onSettingChanged(
                Setting.NOTIFICATIONS,
                isChecked.toInt()
            )

            notifTimeTextView.isEnabled = isChecked
            timeEditText.isEnabled = isChecked
        }

        everyNDaysStrictSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            settingPresenter.onSettingChanged(
                Setting.EVERY_N_DAYS_STRICT,
                isChecked.toInt()
            )
        }

        weekStartsFromSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                 if (++weekStartsFromInteractions > 1)
                     settingPresenter.onSettingChanged(Setting.WEEK_STARTS_MON, (position == 0).toInt())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        delayDaysSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (++delayDaysInteractions > 1)
                settingPresenter.onSettingChanged(Setting.DELAYED_DAYS, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        toDefaultTextView.setOnClickListener {
            showToDefaultDialog()
        }

    }

    private fun showToDefaultDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.default_title))
        builder.setMessage(getString(R.string.to_default_question))

        builder.setPositiveButton("Yes") { dialog, which ->
            settingPresenter.resetAllToDefault()
            refreshSettingViews()
            Toast.makeText(context, getString(R.string.settings_to_default_done), Toast.LENGTH_SHORT).show()

        }

        builder.setNegativeButton("No") { dialog, which ->

        }
        builder.show()
    }

    private fun convertSecondsToTime(seconds: Int): String {
        val hour = seconds/(60*60)
        val minute = (seconds % (60*60))/60
        val hourAsString = addZeroIfOneNumber(hour)
        val minuteAsString = addZeroIfOneNumber(minute)
        return "$hourAsString:$minuteAsString"
    }

    private fun refreshSettingViews() {
        val settingList = settingPresenter.getAllSettingsValue()
        settingList.forEach {
            when (it.first) {
                Setting.WEEK_STARTS_MON -> weekStartsFromSpinner.setSelection((it.second==0).toInt())
                Setting.CHAIN_POINTS -> chainPointsSwitch.isChecked = it.second.toBoolean()
                Setting.SPECIAL_DAYS -> sdSwitch.isChecked =it.second.toBoolean()
                Setting.GRATITUDE -> gratitudeSwitch.isChecked = it.second.toBoolean()
                Setting.NOTIFICATIONS -> {
                    notificationsSwitch.isChecked = it.second.toBoolean()
                    notifTimeTextView.isEnabled = notificationsSwitch.isChecked
                    timeEditText.isEnabled = notificationsSwitch.isChecked}
                Setting.EVERY_N_DAYS_STRICT -> everyNDaysStrictSwitch.isChecked = it.second.toBoolean()
                Setting.DELAYED_DAYS -> delayDaysSpinner.setSelection(it.second)
                Setting.NOTIFICATION_TIME -> timeEditText.setText(convertSecondsToTime(it.second))
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        settingPresenter = DaggerAppComponent.create().settingPresenter()
        refreshSettingViews()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun populateSpinner(spinner: Spinner, array_id: Int) {
        val adapter = ArrayAdapter.createFromResource(activity, array_id, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun showTimePickerDialog(hour: Int, minute: Int) {
        val listener = TimePickerDialog.OnTimeSetListener {
                _, selectedHour, selectedMin ->
            settingPresenter.onSettingChanged(Setting.NOTIFICATION_TIME, (selectedHour*60*60 + selectedMin*60))
            timeEditText.setText("${addZeroIfOneNumber(selectedHour)}:${addZeroIfOneNumber(selectedMin)}")
        }

        TimePickerDialog(activity, listener, hour, minute, true).show()
    }

    private fun addZeroIfOneNumber(number: Int): String {
        val asString = number.toString()
        return if (asString.length > 1) asString
        else "0$asString"
    }
}


