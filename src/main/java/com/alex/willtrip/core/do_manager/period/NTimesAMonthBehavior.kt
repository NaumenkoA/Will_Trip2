package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.interfaces.ResultLoader
import org.threeten.bp.LocalDate

class NTimesAMonthBehavior (val timesAMonth: Int, val loader: ResultLoader):PeriodBehavior() {

    override fun getTypeInfo(): String {
        return "Repeat n times during a month"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val monthStartDate = evaluatedDate.withDayOfMonth(1)
        val results = loader.loadResultsForPeriod(evaluatedDo.id, monthStartDate, evaluatedDate, Result.ResultType.SKIPPED)
        val resultsLeft: Int
        resultsLeft = if (results == null) timesAMonth else (timesAMonth - results.size)
        val daysLeft = (evaluatedDate.lengthOfMonth() - evaluatedDate.dayOfMonth) + 1
        return (resultsLeft >= daysLeft)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        val monthStartDate = evaluatedDate.withDayOfMonth(1)
        val results = loader.loadResultsForPeriod(evaluatedDo.id, monthStartDate, evaluatedDate, Result.ResultType.SKIPPED)
        return (results?.size ?: 0 < timesAMonth)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is NTimesAMonthBehavior) return false
        return (timesAMonth == other.timesAMonth)
    }
}