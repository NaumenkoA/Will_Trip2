package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.interfaces.ResultLoader
import org.threeten.bp.LocalDate

class SingleBehavior(val date: LocalDate, val loader: ResultLoader): PeriodBehavior() {

    override fun getTypeInfo(): String {
        return "Do once at selected date"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {

        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        return (evaluatedDate.isEqual(date))
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        return this.isObligatoryOnDate(evaluatedDo, evaluatedDate)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SingleBehavior) return false
        return (date == other.date)
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}