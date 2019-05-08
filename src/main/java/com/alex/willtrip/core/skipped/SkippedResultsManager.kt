package com.alex.willtrip.core.skipped

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager
import com.alex.willtrip.core.willpower.WPManager
import com.alex.willtrip.objectbox.helpers.DateSaver
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class SkippedResultsManager (val doManager: DoManager, val resultManager: ResultManager,
                             val wPManager: WPManager, val settingManager: SettingsManager,
                             val dateSaver: DateSaver): SkippedResultsChecker {

    override fun checkHasSkippedResults(doId: Long, beforeDate: LocalDate): Boolean {

        val doObj = doManager.getDoById(doId) ?: throw IllegalArgumentException("${SkippedResultsManager::class.java}: " +
                "do object with doId: $doId not found")
        val lastResult = resultManager.getLastResult(doId, Result.ResultType.DEFAULT)

        var checkDate = lastResult?.date ?: doObj.startDate.minusDays(1)

        checkDate =  checkDate.plusDays(1)

        while (checkDate <= beforeDate) {
            if (doObj.isAvailableOnDate(checkDate)) return true
            checkDate =  checkDate.plusDays(1)
        }
        return false
    }

    override fun fillInSkippedResults(beforeDate: LocalDate): Skipped {
        lateinit var dateToCheck: LocalDate
        val lastDateChecked = dateSaver.getDate(100)

        var lostWP = 0
        var skippedNum = 0
        var startDate:LocalDate? = null
        var endDate:LocalDate? = null

        dateToCheck = if (lastDateChecked == null) {
            doManager.getAllDo()?.sortedBy { it.startDate }?.get(0)?.startDate ?: return Skipped (null, null, 0, 0)
        } else lastDateChecked.plusDays(1)

        while (dateToCheck <= beforeDate) {
            val doList = doManager.getActualDoForDate(dateToCheck)
            doList?.forEach {
                if (it.isAvailableOnDate(dateToCheck)) {
                    val result = resultManager.loadResultForDate(it.id, dateToCheck)
                    if (result == null) {
                        val skippedResult = fillInSkippedResult(it, dateToCheck, it.isObligatoryOnDate(dateToCheck))
                        if (skippedResult.resultType == Result.ResultType.DONE) {
                            skippedNum++
                            lostWP += skippedResult.wpPoint + skippedResult.chainPoint
                            if (startDate == null) startDate = skippedResult.date
                            endDate = skippedResult.date
                        }
                    }
                }
            }
                dateToCheck = dateToCheck.plusDays(1)
            }
        wPManager.decreaseWP(lostWP)
        return Skipped(startDate, endDate, skippedNum, lostWP)
        }

    private fun fillInSkippedResult(doObj: Do, dateToFill: LocalDate, isObligatory: Boolean):Result {
        val chainSetting = settingManager.getSettingValue(Setting.CHAIN_POINTS)
        var chainPoint = 0
        if (chainSetting == 1) chainPoint = resultManager.countChainWP(doObj.id, doObj.complexity)

        val wpPoint: Int
        val resultType: Result.ResultType

        if (isObligatory) {
            resultType = Result.ResultType.DONE
            wpPoint = doObj.complexity
        } else {
            resultType = Result.ResultType.SKIPPED
            wpPoint = 0
            chainPoint = 0
        }

        val result = Result(doId = doObj.id, date = dateToFill, resultType = resultType,
            isPositive = false, wpPoint = wpPoint, chainPoint = chainPoint)

        resultManager.addResult(result)

        return result
    }
}

