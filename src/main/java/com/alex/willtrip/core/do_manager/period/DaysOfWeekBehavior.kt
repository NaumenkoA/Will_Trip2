package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.interfaces.PeriodBehavior
import com.alex.willtrip.core.do_manager.interfaces.ResultLoader
import org.threeten.bp.LocalDate

class DaysOfWeekBehavior(private val daysList: List<Int>, private val loader: ResultLoader) : PeriodBehavior {

    override fun getTypeInfo(): String {
        return "Commitment at selected days of week"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.link, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val dayOfWeek = evaluatedDate.dayOfWeek.value
        return daysList.contains(dayOfWeek)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        return this.isObligatoryOnDate(evaluatedDo, evaluatedDate)
    }
}