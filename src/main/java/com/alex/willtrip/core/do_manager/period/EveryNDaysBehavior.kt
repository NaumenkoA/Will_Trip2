package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.interfaces.ResultLoader
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

class EveryNDaysBehavior(val repeatPeriod: Int, val loader: ResultLoader,
                         val settingsManager: SettingsManager) : PeriodBehavior() {

    override fun getTypeInfo(): String {
        return "Repeat every N days"
    }

    override fun isObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        return evaluateObligatoryOnDate(evaluatedDo, evaluatedDate)
    }

    override fun isAvailableOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        if (evaluatedDate !in evaluatedDo.startDate..evaluatedDo.expireDate) return false
        val evaluatedDayResult = loader.loadResultForDate(evaluatedDo.id, evaluatedDate)
        if (evaluatedDayResult != null) return false

        return when (settingsManager.getSettingValue(Setting.EVERY_N_DAYS_STRICT)) {
            0 -> {
                true
            }
            else -> {
                loader.getLastResult(evaluatedDo.id, Result.ResultType.SKIPPED) ?: return true
                evaluateObligatoryOnDate(evaluatedDo, evaluatedDate)
            }
        }
    }

    private fun evaluateObligatoryOnDate(evaluatedDo: Do, evaluatedDate: LocalDate): Boolean {
        val lastResult = loader.getLastResult(evaluatedDo.id, Result.ResultType.SKIPPED)
        return if (lastResult == null) {
            ChronoUnit.DAYS.between(evaluatedDo.startDate, evaluatedDate.plusDays(1)) >= repeatPeriod
        } else
            (ChronoUnit.DAYS.between(lastResult.date, evaluatedDate) >= repeatPeriod)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is EveryNDaysBehavior) return false
        return (repeatPeriod == other.repeatPeriod)
    }

    override fun hashCode(): Int {
        var result = repeatPeriod
        result = 31 * result + loader.hashCode()
        result = 31 * result + settingsManager.hashCode()
        return result
    }
}
