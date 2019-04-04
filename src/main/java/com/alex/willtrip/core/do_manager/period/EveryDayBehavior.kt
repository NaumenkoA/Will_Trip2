package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.interfaces.ResultLoader
import org.threeten.bp.LocalDate

class EveryDayBehavior(private val loader: ResultLoader) : PeriodBehavior() {

    override fun getTypeInfo(): String = "Everyday"

    override fun isObligatoryOnDate(evaluatedDo: Do,  evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false

        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        return true
    }

    override fun isAvailableOnDate(evaluatedDo: Do,  evaluatedDate: LocalDate): Boolean {
        return this.isObligatoryOnDate(evaluatedDo, evaluatedDate)
    }

    override fun equals(other: Any?): Boolean {
        return (other is EveryDayBehavior)
    }
}