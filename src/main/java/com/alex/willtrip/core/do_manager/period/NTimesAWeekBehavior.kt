package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.Result
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
        val weekStartDate = evaluatedDate.minusDays(currentDayIndex.toLong()-1)
        val results = loader.loadResultsForPeriod(evaluatedDo.id, weekStartDate, evaluatedDate, Result.ResultType.SKIPPED)
        val resultsLeft: Int
        if (results == null) resultsLeft = timesAWeek else resultsLeft = timesAWeek - results.size
        val daysLeft = (7 - currentDayIndex) + 1
        return (resultsLeft >= daysLeft)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val currentDayIndex = evaluatedDate.dayOfWeek.value
        val weekStartDate = evaluatedDate.minusDays(currentDayIndex.toLong()-1)
        val results = loader.loadResultsForPeriod(evaluatedDo.id, weekStartDate, evaluatedDate, Result.ResultType.SKIPPED)
        return (results?.size ?: 0 < timesAWeek)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NTimesAWeekBehavior) return false
        return (timesAWeek == other.timesAWeek)
    }

    override fun hashCode(): Int {
        var result = timesAWeek
        result = 31 * result + loader.hashCode()
        return result
    }
}