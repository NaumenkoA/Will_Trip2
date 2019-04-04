package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.interfaces.ResultLoader
import org.threeten.bp.LocalDate

class NTimesAWeekBehavior (val timesAWeek: Int, val loader: ResultLoader):PeriodBehavior() {

    override fun getTypeInfo(): String {
        return "Repeat n times during a week"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val currentDayIndex = evaluatedDate.dayOfWeek.value
        val results = loader.getLastNonSkippedResults(evaluatedDo.id, currentDayIndex - 1)
        val resultsLeft = timesAWeek - results.size
        val daysLeft = (7 - currentDayIndex) + 1
        return (resultsLeft >= daysLeft)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val currentDayIndex = evaluatedDate.dayOfWeek.value
        val results = loader.getLastNonSkippedResults(evaluatedDo.id, currentDayIndex - 1)
        return (results.size < timesAWeek)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NTimesAWeekBehavior) return false
        return (timesAWeek == other.timesAWeek)
    }
}