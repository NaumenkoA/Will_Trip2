package com.alex.willtrip.ui


import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker

import com.alex.willtrip.R
import com.alex.willtrip.ui.adapter.FutureActionAdapter
import com.alex.willtrip.ui.adapter.ResultActionAdapter
import com.alex.willtrip.ui.adapter.TodayActionAdapter
import com.alex.willtrip.ui.adapter.data.OpenAction
import com.alex.willtrip.ui.adapter.data.ResultAction
import com.alex.willtrip.ui.viewModel.ResultsViewModel
import com.alex.willtrip.ui.viewModel.StoryViewModel
import kotlinx.android.synthetic.main.fragment_results.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.text.DateFormatSymbols
import java.util.*

class ResultsFragment : Fragment(), TabLayout.OnTabSelectedListener, DatePickerDialog.OnDateSetListener,

    TodayActionAdapter.OnResultSelectedListener, ResultActionAdapter.ResultOnClickListener {

    private var currentDate: LocalDate = getDate()
    private lateinit var selectedDate: LocalDate
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: ResultsViewModel
    private val weekNames = DateFormatSymbols(Locale.US).shortWeekdays
    private val monthNames = DateFormatSymbols(Locale.US).months


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tab_layout.addOnTabSelectedListener(this)

        viewModel =  ViewModelProviders.of(this).get(ResultsViewModel::class.java)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        selectedDate = getDate()
        dateTextView.text = selectedDate.toText()

        dateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        if (tab_layout.selectedTabPosition == -1) {
            val tab = tab_layout.getTabAt(0)
            tab?.select()
        }
}

    override fun onResultSelected(action: OpenAction, resultSelected: String, note: String) {
        viewModel.addResult(action, currentDate, resultSelected, note)
    }

    override fun onUndoButtonClicked(id: Long) {
        viewModel.removeResult (selectedDate, id)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val newSelectedDate = LocalDate.of(year, month, dayOfMonth)
        if (newSelectedDate != selectedDate){
            selectedDate = newSelectedDate
            dateTextView.text = selectedDate.toText()

            if (tab_layout.getTabAt(0)!!.isSelected) showRecyclerActions()
            if (tab_layout.getTabAt(1)!!.isSelected) showRecyclerResults()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(context!!, this, selectedDate.year, selectedDate.month.value, selectedDate.dayOfMonth)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> showRecyclerActions()
            1 -> showRecyclerResults()
        }
    }

    private fun showRecyclerResults() {
        val delayedDays: Int = viewModel.getDelayedDays()
        val isUndoEnabled = (selectedDate >= currentDate.minusDays(delayedDays.toLong()))

        val results: List <ResultAction> = viewModel.getResultsForDate(selectedDate)

        recyclerView.adapter = ResultActionAdapter(context!!, results, isUndoEnabled, this)
    }

    private fun showRecyclerActions() {
        val actions: List <OpenAction> = viewModel.getActionsForDate(selectedDate)

        if (selectedDate > currentDate) {
            recyclerView.adapter = FutureActionAdapter(context!!, actions)
        } else {

            recyclerView.adapter = TodayActionAdapter(context!!, actions, this)
        }
    }

    private fun getDate(): LocalDate {
        val currentDate = Calendar.getInstance().time
        return Instant.ofEpochMilli(currentDate.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun LocalDate.toText(): String {
        if (this == currentDate) return "Today"
        if (this.minusDays(1) == currentDate) return "Tomorrow"
        if (this.plusDays(1) == currentDate) return "Yesterday"

        val dayOfWeek = weekNames[this.dayOfWeek.value - 1]
        val month = monthNames[this.monthValue - 1]
        val day = this.dayOfMonth

        return "$month $day ($dayOfWeek)"
    }
}


