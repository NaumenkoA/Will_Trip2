package com.alex.willtrip.ui.viewModel

import android.arch.lifecycle.ViewModel
import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.di.*
import com.alex.willtrip.ui.adapter.TodayActionAdapter
import com.alex.willtrip.ui.adapter.data.OpenAction
import com.alex.willtrip.ui.adapter.data.ResultAction
import org.threeten.bp.LocalDate

class ResultsViewModel: ViewModel() {
    private var doManager = DaggerAppComponent.create().doManager()
    private var resultManager = DaggerAppComponent.create().resultManager()
    private var settingManager = DaggerAppComponent.create().settingsManager()

    init {
        val doObj1 = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 3), expireDate = LocalDate.of(2019, 12, 31))

        val everyNDays = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(3)).build().everyNDaysBehavior()
        val doObj2 = Do (name="Test 2", periodBehavior = everyNDays,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 4, 3), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test 3", periodBehavior = DaggerDaysOfWeekBehaviorComponent.builder().
            daysOfWeekBehaviorModule(DaysOfWeekBehaviorModule(listOf(1,2,3))).build().daysOfWeekBehavior(),
            note = "Test do3", isSpecialDayEnabled = true, isPositive = false, complexity = 1,
            startDate = LocalDate.of(2019, 5, 4), expireDate = LocalDate.of(2020, 12, 31))

        val doObj4 = Do (name="Test 4", periodBehavior = DaggerNTimesAWeekBehaviorComponent.builder().
            nTimesAWeekBehaviorModule(NTimesAWeekBehaviorModule(4)).build().nTimesAWeekBehavior(),
            note = "Test do4", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 6, 10), expireDate = LocalDate.of(2021, 12, 31))

        val doObj5 = Do (name="Test 5", periodBehavior = DaggerSingleBehaviorComponent.builder().
            singleBehaviorModule(SingleBehaviorModule(LocalDate.of(2019,6,10))).build().singleBehavior(),
            note = "Test do5", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 6, 10), expireDate = LocalDate.of(2021, 6, 10))

        val doObj6 = Do (name="Test 6", periodBehavior = DaggerNTimesAMonthBehaviorComponent.builder().
            nTimesAMonthBehaviorModule(NTimesAMonthBehaviorModule(4)).build().nTimesAMonthBehavior(),
            note = "Test do6", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 6, 10), expireDate = LocalDate.of(2021, 10, 10))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)
        doManager.addNewDo(doObj5)
        doManager.addNewDo(doObj6)
    }

    fun getDelayedDays(): Int {
        return settingManager.getSettingValue(Setting.DELAYED_DAYS)
    }

    fun addResult(action: OpenAction, date: LocalDate, result: String, note: String) {
        val newResult = createResultFromAction (action, date, result, note)
        resultManager.addResult(newResult)
    }

    fun removeResult (date: LocalDate, doId: Long) {
        resultManager.removeResult(doId, date)
    }

    fun getResultsForDate(date: LocalDate): List <ResultAction> {
        val doList = doManager.getActualDoForDate(date)
        val results = mutableListOf<ResultAction>()

        doList?.forEach {
            val result = resultManager.loadResultForDate(it.id, date)
            if (result != null) {
                val resultAction = createResultAction (it.id, it.name, result)
                results.add(resultAction)
            }
        }
        return results
    }

    fun getActionsForDate (date: LocalDate): List <OpenAction>{
        val doList = doManager.getActualDoForDate(date)
        val actions = mutableListOf<OpenAction>()

        doList?.forEach {

            if (it.isObligatoryOnDate(date)) {
                val chainWP = resultManager.countChainWP(it.id, it.complexity)
                val chainLength = resultManager.getCurrentChain(it.id)?.length ?: 0

                val openAction = OpenAction(doId = it.id, name = it.name, isObligatory = true, isPositive = it.isPositive,
                    complexity = it.complexity, chainWP = chainWP, chainLength = chainLength, isSpecialDayEnabled = it.isSpecialDayEnabled)

                actions.add(openAction)
            } else if (it.isAvailableOnDate(date)) {
                val chainWP = resultManager.countChainWP(it.id, it.complexity)
                val chainLength = resultManager.getCurrentChain(it.id)?.length ?: 0

                val openAction = OpenAction(doId = it.id, name = it.name, isObligatory = false, isPositive = it.isPositive,
                    complexity = it.complexity, chainWP = chainWP, chainLength = chainLength, isSpecialDayEnabled = it.isSpecialDayEnabled)

                actions.add(openAction)
            }
        }

        return actions
    }

    private fun createResultAction(id: Long, name: String, result: Result): ResultAction {
        val type = when (result.resultType) {
            Result.ResultType.SPECIAL_DAY -> ResultAction.SPECIAL_DAY
            Result.ResultType.SKIPPED -> ResultAction.SKIPPED
            Result.ResultType.DONE -> {
                if (result.isPositive) ResultAction.SUCCESS
                else ResultAction.FAIL
            }
           else -> ResultAction.DEFAULT
        }

        return ResultAction(doId = id, name = name, type = type,  complexity = result.wpPoint, chainWP = result.chainPoint, note = result.note)
    }

    private fun createResultFromAction(action: OpenAction, date: LocalDate, result: String, note: String): Result {

        val isPositive = (action.isPositive && result == TodayActionAdapter.DONE) ||
                (!action.isPositive && result== TodayActionAdapter.NOT_DONE)

        val resultType = when (result) {
            TodayActionAdapter.DONE -> Result.ResultType.DONE
            TodayActionAdapter.NOT_DONE -> Result.ResultType.DONE
            TodayActionAdapter.SKIPPED -> Result.ResultType.SKIPPED
            TodayActionAdapter.SPECIAL_DAY -> Result.ResultType.SPECIAL_DAY
            else -> Result.ResultType.DEFAULT
        }

        return Result (doId = action.doId, date = date, isPositive = isPositive, resultType = resultType,
            wpPoint = action.complexity, chainPoint = action.chainWP, note = note)
    }
}