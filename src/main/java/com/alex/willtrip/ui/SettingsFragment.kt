package com.alex.willtrip.ui


import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.alex.willtrip.R
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.ui.viewModel.SettingViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : Fragment(), Observer<Pair<Setting, Int>> {

    lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData().observe(this, this)

        populateSpinner (weekStartsFromSpinner, R.array.array_week_starts_from)
        populateSpinner (delayDaysSpinner, R.array.array_delay_days)

        timeEditText.inputType = InputType.TYPE_NULL;
        timeEditText.requestFocus()
        
        timeEditText.setOnClickListener {
            val hour = timeEditText.text.split(":")[0]
            val minute = timeEditText.text.split(":")[1]

            showTimePickerDialog(hour.toInt(), minute.toInt());
        }
    }

    override fun onChanged(data: Pair<Setting, Int>?) {
        when (data?.first) {

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(SettingViewModel::class.java)
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
                view, selectedHour, selectedMin ->
                val hourAsString = addZeroIfOneNumber(selectedHour)
                val minuteAsString = addZeroIfOneNumber(selectedMin)
                timeEditText.setText("$hourAsString:$minuteAsString")
        }

        TimePickerDialog(activity, listener, hour, minute, true).show()
    }

    private fun addZeroIfOneNumber(number: Int): String {
        val asString = number.toString()
        return if (asString.length > 1) asString
        else "0$asString"
    }
}
