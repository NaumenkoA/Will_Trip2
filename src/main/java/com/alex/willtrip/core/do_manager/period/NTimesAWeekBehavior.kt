package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.interfaces.PeriodBehavior
import com.alex.willtrip.core.do_manager.interfaces.ResultLoader
import org.threeten.bp.LocalDate

class NTimesAWeekBehavior (private val timesAWeek: Int, private val loader: ResultLoader):PeriodBehavior {

    override fun getTypeInfo(): String {
        return "Repeat n times during a week"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.link, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val currentDayIndex = evaluatedDate.dayOfWeek.value
        val results = loader.getLastResults(currentDayIndex - 1)
        val resultsLeft = timesAWeek - results.size
        val daysLeft = (7 - currentDayIndex) + 1
        return (resultsLeft >= daysLeft)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.link, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val currentDayIndex = evaluatedDate.dayOfWeek.value
        val results = loader.getLastResults(currentDayIndex - 1)
        return (results.size < timesAWeek)
    }
}