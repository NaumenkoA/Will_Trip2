package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.interfaces.PeriodBehavior
import com.alex.willtrip.core.do_manager.interfaces.ResultLoader
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

class EveryNDaysPeriod(val repeatPeriod: Int, val loader: ResultLoader, val settingsManager: SettingsManager) : PeriodBehavior {

    override fun getTypeInfo(): String {
        return "Repeat every N days"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.link, evaluatedDate)
        if (evaluatedDayResult != null) return false

        return evaluateObligatoryOnDate(evaluatedDo, evaluatedDate)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.link, evaluatedDate)
        if (evaluatedDayResult != null) return false

        return when (settingsManager.getSettingValue(Setting.EVERY_N_DAYS_STRICT.name)) {
            0 -> {
                true
            }
            else -> {
                evaluateObligatoryOnDate(evaluatedDo, evaluatedDate)
            }
        }
    }

    private fun evaluateObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        val lastResult = loader.getLastResults(1)
        if (lastResult.isEmpty()) return true

        return (ChronoUnit.DAYS.between(lastResult[0].date, evaluatedDate) >= repeatPeriod - 1 )
    }
}
