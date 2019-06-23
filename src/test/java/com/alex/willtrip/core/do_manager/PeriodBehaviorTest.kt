package com.alex.willtrip.core.do_manager

import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.implementations.ResultLoaderImp
import com.alex.willtrip.core.result.implementations.ResultMutatorImp
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.di.*
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate


class PeriodBehaviorTest: AbstractObjectBoxTest() {

    lateinit var doManager: DoManager
    val resultMutator = ResultMutatorImp()

    @Before
    fun setUp() {
        doManager = DaggerAppComponent.builder().build().doManager()
    }

    @Test
    fun singleBehaviorTest(){
        val doSingleDay = Do (name="Test", periodBehavior = DaggerSingleBehaviorComponent.builder().
            singleBehaviorModule(SingleBehaviorModule(LocalDate.of(2019,6,24))).build().singleBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 6, 24), expireDate = LocalDate.of(2019, 6, 24))

        val isOblBeforeStart = doSingleDay.isObligatoryOnDate(LocalDate.of(2019, 6,23))
        val isAvailBeforeStart = doSingleDay.isAvailableOnDate(LocalDate.of(2019, 6, 23))
        val isOblOnStart = doSingleDay.isObligatoryOnDate(LocalDate.of(2019, 6,24))
        val isAvailOnStart = doSingleDay.isAvailableOnDate(LocalDate.of(2019, 6,24))
        val isOblAfterEnd = doSingleDay.isObligatoryOnDate(LocalDate.of(2019, 6,25))
        val isAvailAfterEnd = doSingleDay.isAvailableOnDate(LocalDate.of(2019, 6,25))

        doSingleDay.id = doManager.addNewDo(doSingleDay)

        val result1 = Result(doId = doSingleDay.id, date = LocalDate.of(2019, 6, 24),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result1)

        val isOblOnResult1Day = doSingleDay.isObligatoryOnDate(LocalDate.of(2019, 6, 24))
        val isAvailOnResult1Day = doSingleDay.isAvailableOnDate(LocalDate.of(2019, 6, 24))

        assertThat(isAvailBeforeStart).isEqualTo(false)
        assertThat(isOblBeforeStart).isEqualTo(false)
        assertThat(isOblOnStart).isEqualTo(true)
        assertThat(isAvailOnStart).isEqualTo(true)
        assertThat(isAvailAfterEnd).isEqualTo(false)
        assertThat(isOblAfterEnd).isEqualTo(false)
        assertThat(isOblOnResult1Day).isEqualTo(false)
        assertThat(isAvailOnResult1Day).isEqualTo(false)
    }

    @Test
    fun everyDayBehaviorTest() {
        val doEveryDay = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.builder().build().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val isOblBeforeStart = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 3,1))
        val isAvailBeforeStart = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 3, 1))
        val isOblOnStart = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 4,1))
        val isAvailOnStart = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 4,1))
        val isOblDuringPeriod = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 6,1))
        val isAvailDuringPeriod = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 6,1))
        val isOblOnEnd = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 12,31))
        val isAvailOnEnd = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 12,31))
        val isOblAfterEnd = doEveryDay.isObligatoryOnDate(LocalDate.of(2020, 12,31))
        val isAvailAfterEnd = doEveryDay.isAvailableOnDate(LocalDate.of(2020, 12,31))

        doEveryDay.id = doManager.addNewDo(doEveryDay)

        val result1 = Result(doId = doEveryDay.id, date = LocalDate.of(2019, 5, 1),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result1)

        val isOblOnResult1Day = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 5,1))
        val isAvailOnResult1Day = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 5,1))

        val result2 = Result(doId = doEveryDay.id, date = LocalDate.of(2019, 5, 2),
            resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result2)

        val isOblOnResult2Day = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 5,2))
        val isAvailOnResult2Day = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 5,2))

        val result3 = Result(doId = doEveryDay.id, date = LocalDate.of(2019, 5, 3),
            resultType = Result.ResultType.SPECIAL_DAY, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result3)

        val isOblOnResult3Day = doEveryDay.isObligatoryOnDate(LocalDate.of(2019, 5,3))
        val isAvailOnResult3Day = doEveryDay.isAvailableOnDate(LocalDate.of(2019, 5,3))

        assertThat(isAvailBeforeStart).isEqualTo(false)
        assertThat(isOblBeforeStart).isEqualTo(false)
        assertThat(isOblOnStart).isEqualTo(true)
        assertThat(isAvailOnStart).isEqualTo(true)
        assertThat(isAvailDuringPeriod).isEqualTo(true)
        assertThat(isOblDuringPeriod).isEqualTo(true)
        assertThat(isOblOnEnd).isEqualTo(true)
        assertThat(isAvailOnEnd).isEqualTo(true)
        assertThat(isAvailAfterEnd).isEqualTo(false)
        assertThat(isOblAfterEnd).isEqualTo(false)
        assertThat(isOblOnResult1Day).isEqualTo(false)
        assertThat(isAvailOnResult1Day).isEqualTo(false)
        assertThat(isOblOnResult2Day).isEqualTo(false)
        assertThat(isAvailOnResult2Day).isEqualTo(false)
        assertThat(isOblOnResult3Day).isEqualTo(false)
        assertThat(isAvailOnResult3Day).isEqualTo(false)
    }

    @Test
    fun daysOfWeekBehaviorTest() {
        val doDaysOfWeek = Do (name="Test 3", periodBehavior = DaggerDaysOfWeekBehaviorComponent.builder().
            daysOfWeekBehaviorModule(DaysOfWeekBehaviorModule(listOf(1,3,6))).build().daysOfWeekBehavior(),
            note = "Test do3", isSpecialDayEnabled = true, isPositive = false, complexity = 1,
            startDate = LocalDate.of(2019, 5, 1), expireDate = LocalDate.of(2020, 12, 31))

        val isOblBeforeStart = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 3,1))
        val isAvailBeforeStart = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 3, 1))
        val isOblOnStart = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,1))
        val isAvailOnStart = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,1))
        val isOblOnEnd = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2020, 12,31))
        val isAvailOnEnd = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2020, 12,31))
        val isOblAfterEnd = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2021, 12,31))
        val isAvailAfterEnd = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2021, 12,31))

        doDaysOfWeek.id = doManager.addNewDo(doDaysOfWeek)

        val isAvailOnMon = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,6))
        val isOblOnMon = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,6))
        val isAvailOnTue = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,7))
        val isOblOnTue = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,7))
        val isAvailOnWed = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,8))
        val isOblOnWed = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,8))
        val isAvailOnThu = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,9))
        val isOblOnThu = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,9))
        val isAvailOnFri = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,10))
        val isOblOnFri = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,10))
        val isAvailOnSat = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,11))
        val isOblOnSat = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,11))
        val isAvailOnSun = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,12))
        val isOblOnSun= doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,12))

        val result1 = Result(doId = doDaysOfWeek.id, date = LocalDate.of(2019, 5, 8),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result1)

        val isOblOnResult1Day = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,8))
        val isAvailOnResult1Day = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,8))

        val result2 = Result(doId = doDaysOfWeek.id, date = LocalDate.of(2019, 5, 6),
            resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result2)

        val isOblOnResult2Day = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,6))
        val isAvailOnResult2Day = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,6))

        val result3 = Result(doId = doDaysOfWeek.id, date = LocalDate.of(2019, 5, 11),
            resultType = Result.ResultType.SPECIAL_DAY, isPositive = false, wpPoint = 5, chainPoint = 1)

        resultMutator.addResult(result3)

        val isOblOnResult3Day = doDaysOfWeek.isObligatoryOnDate(LocalDate.of(2019, 5,11))
        val isAvailOnResult3Day = doDaysOfWeek.isAvailableOnDate(LocalDate.of(2019, 5,11))

        assertThat(isAvailBeforeStart).isEqualTo(false)
        assertThat(isOblBeforeStart).isEqualTo(false)
        assertThat(isOblOnStart).isEqualTo(true)
        assertThat(isAvailOnStart).isEqualTo(true)
        assertThat(isOblOnEnd).isEqualTo(false)
        assertThat(isAvailOnEnd).isEqualTo(false)
        assertThat(isAvailAfterEnd).isEqualTo(false)
        assertThat(isOblAfterEnd).isEqualTo(false)
        assertThat(isOblOnResult1Day).isEqualTo(false)
        assertThat(isAvailOnResult1Day).isEqualTo(false)
        assertThat(isOblOnResult2Day).isEqualTo(false)
        assertThat(isAvailOnResult2Day).isEqualTo(false)
        assertThat(isOblOnResult3Day).isEqualTo(false)
        assertThat(isAvailOnResult3Day).isEqualTo(false)

        assertThat(isAvailOnMon).isTrue()
        assertThat(isAvailOnTue).isFalse()
        assertThat(isAvailOnWed).isTrue()
        assertThat(isAvailOnThu).isFalse()
        assertThat(isAvailOnFri).isFalse()
        assertThat(isAvailOnSat).isTrue()
        assertThat(isAvailOnSun).isFalse()

        assertThat(isOblOnMon).isTrue()
        assertThat(isOblOnTue).isFalse()
        assertThat(isOblOnWed).isTrue()
        assertThat(isOblOnThu).isFalse()
        assertThat(isOblOnFri).isFalse()
        assertThat(isOblOnSat).isTrue()
        assertThat(isOblOnSun).isFalse()
    }

    @Test
    fun nTimesAWeekBehaviorTest() {
        val nTimesAWeekBeh = Do (name="Test 4", periodBehavior = DaggerNTimesAWeekBehaviorComponent.builder().
            nTimesAWeekBehaviorModule(NTimesAWeekBehaviorModule(3)).build().nTimesAWeekBehavior(),
            note = "Test do4", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        nTimesAWeekBeh.id = doManager.addNewDo(nTimesAWeekBeh)

        val isOblBeforeStart = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 3,1))
        val isAvailBeforeStart = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 3, 1))
        val isOblOnStart = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 4,1))
        val isAvailOnStart = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 4,1))
        val isOblOnEnd = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 12,31))
        val isAvailOnEnd = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 12,31))
        val isOblAfterEnd = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2021, 12,31))
        val isAvailAfterEnd = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2021, 12,31))

        val isAvailOnMon = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,6))
        val isOblOnMon = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,6))

        val result0 = Result(doId = nTimesAWeekBeh.id, date = LocalDate.of(2019, 5, 6),
            resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 0, chainPoint =0)
        resultMutator.addResult(result0)

        val isAvailOnMonAftResult = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,6))
        val isOblOnMonAftResult = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,6))

        val isAvailOnTue = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,7))
        val isOblOnTue = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,7))

        val result1 = Result(doId = nTimesAWeekBeh.id, date = LocalDate.of(2019, 5, 7),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result1)

        val isAvailOnWed = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,8))
        val isOblOnWed = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,8))

        val result2 = Result(doId = nTimesAWeekBeh.id, date = LocalDate.of(2019, 5, 8),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result2)

        val isAvailOnResultDay = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,8))
        val isOblOnResultDay = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,8))

        val isAvailOnThu = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,9))
        val isOblOnThu = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,9))

        val result3 = Result(doId = nTimesAWeekBeh.id, date = LocalDate.of(2019, 5, 9),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result3)

        val isAvailOnFri = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,10))
        val isOblOnFri = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,10))

        resultMutator.removeResult(result2)
        resultMutator.removeResult(result3)

        val isAvailOnSat = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,11))
        val isOblOnSat = nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,11))
        val isAvailOnSun = nTimesAWeekBeh.isAvailableOnDate(LocalDate.of(2019, 5,12))
        val isOblOnSun= nTimesAWeekBeh.isObligatoryOnDate(LocalDate.of(2019, 5,12))

        assertThat(isAvailBeforeStart).isFalse()
        assertThat(isOblBeforeStart).isFalse()
        assertThat(isOblOnStart).isFalse()
        assertThat(isAvailOnStart).isTrue()
        assertThat(isOblOnEnd).isFalse()
        assertThat(isAvailOnEnd).isTrue()
        assertThat(isAvailAfterEnd).isFalse()
        assertThat(isOblAfterEnd).isFalse()
        assertThat(isOblOnResultDay).isFalse()
        assertThat(isAvailOnResultDay).isFalse()

        assertThat(isAvailOnMon).isTrue()
        assertThat(isAvailOnMonAftResult).isFalse()
        assertThat(isAvailOnTue).isTrue()
        assertThat(isAvailOnWed).isTrue()
        assertThat(isAvailOnThu).isTrue()
        assertThat(isAvailOnFri).isFalse()
        assertThat(isAvailOnSat).isTrue()
        assertThat(isAvailOnSun).isTrue()

        assertThat(isOblOnMon).isFalse()
        assertThat(isOblOnMonAftResult).isFalse()
        assertThat(isOblOnTue).isFalse()
        assertThat(isOblOnWed).isFalse()
        assertThat(isOblOnThu).isFalse()
        assertThat(isOblOnFri).isFalse()
        assertThat(isOblOnSat).isTrue()
        assertThat(isOblOnSun).isTrue()
    }

    @Test
    fun nTimesAMonthBehaviorTest() {
        val nTimesAMonthBeh = Do (name="Test 4", periodBehavior = DaggerNTimesAMonthBehaviorComponent.builder().
            nTimesAMonthBehaviorModule(NTimesAMonthBehaviorModule(4)).build().nTimesAMonthBehavior(),
            note = "Test do4", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        nTimesAMonthBeh.id = doManager.addNewDo(nTimesAMonthBeh)

        val isOblBeforeStart = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 3,31))
        val isAvailBeforeStart = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 3, 31))
        val isOblOnStart = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 4,1))
        val isAvailOnStart = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 4,1))
        val isOblOnEnd = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 12,31))
        val isAvailOnEnd = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 12,31))
        val isOblAfterEnd = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2020, 1,1))
        val isAvailAfterEnd = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2020, 1,1))

        val isAvailOnFirst = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,1))
        val isOblOnFirst = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,1))

        val isAvailOnSecond = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,2))
        val isOblOnSecond = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,2))

        val result0 = Result(doId = nTimesAMonthBeh.id, date = LocalDate.of(2019, 5, 2),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 4, chainPoint =0)
        resultMutator.addResult(result0)

        val isAvailOnTenth = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,10))
        val isOblOnTenth = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,10))

        val result1 = Result(doId = nTimesAMonthBeh.id, date = LocalDate.of(2019, 5, 10),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 4, chainPoint =0)
        resultMutator.addResult(result1)

        val isAvailOnTenthAfterResult = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,10))
        val isOblOnTenthAfterResult = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,10))

        val isAvailOnFifth = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,15))
        val isOblOnFifth = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,15))

        val result2 = Result(doId = nTimesAMonthBeh.id, date = LocalDate.of(2019, 5, 15),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 4, chainPoint =0)
        resultMutator.addResult(result2)

        val isAvailOnTwen = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,20))
        val isOblOnTwen = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,20))

        val result3 = Result(doId = nTimesAMonthBeh.id, date = LocalDate.of(2019, 5, 20),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 4, chainPoint =0)
        resultMutator.addResult(result3)

        val isAvailOnTwenF = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,25))
        val isOblOnTwenF = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,25))

        resultMutator.removeResult(result2)
        resultMutator.removeResult(result3)

        val isAvailOnThirt = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,30))
        val isOblOnThirt = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,30))

        val isAvailOnThirtF = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 5,31))
        val isOblOnThirtF = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 5,31))

        val isAvailOnNewMonth = nTimesAMonthBeh.isAvailableOnDate(LocalDate.of(2019, 6,1))
        val isOblOnNewMonth = nTimesAMonthBeh.isObligatoryOnDate(LocalDate.of(2019, 6,1))

        assertThat(isAvailBeforeStart).isFalse()
        assertThat(isOblBeforeStart).isFalse()
        assertThat(isOblOnStart).isFalse()
        assertThat(isAvailOnStart).isTrue()
        assertThat(isOblOnEnd).isTrue()
        assertThat(isAvailOnEnd).isTrue()
        assertThat(isAvailAfterEnd).isFalse()
        assertThat(isOblAfterEnd).isFalse()
        assertThat(isOblOnTenthAfterResult).isFalse()
        assertThat(isAvailOnTenthAfterResult).isFalse()

        assertThat(isAvailOnFirst).isTrue()
        assertThat(isAvailOnSecond).isTrue()
        assertThat(isAvailOnFifth).isTrue()
        assertThat(isAvailOnTenth).isTrue()
        assertThat(isAvailOnTwen).isTrue()
        assertThat(isAvailOnTwenF).isFalse()
        assertThat(isAvailOnThirt).isTrue()
        assertThat(isAvailOnThirtF).isTrue()
        assertThat(isAvailOnNewMonth).isTrue()

        assertThat(isOblOnFirst).isFalse()
        assertThat(isOblOnSecond).isFalse()
        assertThat(isOblOnFifth).isFalse()
        assertThat(isOblOnTenth).isFalse()
        assertThat(isOblOnTwen).isFalse()
        assertThat(isOblOnTwenF).isFalse()
        assertThat(isOblOnThirt).isTrue()
        assertThat(isOblOnThirtF).isTrue()
        assertThat(isOblOnNewMonth).isFalse()
    }

    @Test
    fun testEveryNDaysNotStrict() {
        val behavior = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(3))
            .build().everyNDaysBehavior()

        val everyNDays = Do (name="Test 2", periodBehavior = behavior ,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        everyNDays.id = doManager.addNewDo(everyNDays)

        val settingManager = DaggerAppComponent.builder().build().settingsManager()

        settingManager.setSettingValue(Setting.EVERY_N_DAYS_STRICT, 0)

        val isOblBeforeStart = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 3,1))
        val isAvailBeforeStart = everyNDays.isAvailableOnDate(LocalDate.of(2019, 3, 1))
        val isOblOnEnd = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 12,31))
        val isAvailOnEnd = everyNDays.isAvailableOnDate(LocalDate.of(2019, 12,31))
        val isOblAfterEnd = everyNDays.isObligatoryOnDate(LocalDate.of(2021, 12,31))
        val isAvailAfterEnd = everyNDays.isAvailableOnDate(LocalDate.of(2021, 12,31))

        val isAvailOnFirst = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,1))
        val isOblOnFirst = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,1))

        val result0 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 1),
            resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result0)

        val isAvailOnFirstAfterResult = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,1))
        val isOblOnFirstAfterResult = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,1))

        val isAvailOnSec = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,2))
        val isOblOnSec = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,2))
        val isAvailOnThird = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,3))
        val isOblOnThird = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,3))

        val result1 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 3),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result1)

        val isAvailOnFour = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,4))
        val isOblOnFour = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,4))
        val isAvailOnFive = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,5))
        val isOblOnFive = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,5))

        val result2 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 5),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 3, chainPoint = 1)
        resultMutator.addResult(result2)

        val isAvailOnResultDay = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,5))
        val isOblOnResultDay = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,5))

        val isAvailOnSix = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,6))
        val isOblOnSix = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,6))
        val isAvailOnSev = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,7))
        val isOblOnSev = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,7))
        val isAvailOnEight = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,8))
        val isOblOnEight = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,8))

        val result3 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 6),
            resultType = Result.ResultType.SPECIAL_DAY, isPositive = false, wpPoint = 3, chainPoint = 1)
        resultMutator.addResult(result3)

        val isAvailOnEightAfterSD = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,8))
        val isOblOnEightAfterSD = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,8))

        val isAvailOnNine = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,9))
        val isOblOnNine = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,9))

        assertThat(isAvailBeforeStart).isFalse()
        assertThat(isOblBeforeStart).isFalse()
        assertThat(isOblOnEnd).isTrue()
        assertThat(isAvailOnEnd).isTrue()
        assertThat(isAvailAfterEnd).isFalse()
        assertThat(isOblAfterEnd).isFalse()
        assertThat(isOblOnResultDay).isFalse()
        assertThat(isAvailOnResultDay).isFalse()

        assertThat(isAvailOnFirst).isTrue()
        assertThat(isOblOnFirst).isFalse()
        assertThat(isAvailOnFirstAfterResult).isFalse()
        assertThat(isOblOnFirstAfterResult).isFalse()
        assertThat(isAvailOnSec).isTrue()
        assertThat(isOblOnSec).isFalse()
        assertThat(isAvailOnThird).isTrue()
        assertThat(isOblOnThird).isTrue()
        assertThat(isAvailOnFour).isTrue()
        assertThat(isOblOnFour).isFalse()
        assertThat(isAvailOnFive).isTrue()
        assertThat(isOblOnFive).isFalse()
        assertThat(isAvailOnSix).isTrue()
        assertThat(isOblOnSix).isFalse()
        assertThat(isAvailOnSev).isTrue()
        assertThat(isOblOnSev).isFalse()
        assertThat(isAvailOnEight).isTrue()
        assertThat(isOblOnEight).isTrue()
        assertThat(isAvailOnEightAfterSD).isTrue()
        assertThat(isOblOnEightAfterSD).isFalse()
        assertThat(isAvailOnNine).isTrue()
        assertThat(isOblOnNine).isTrue()
    }

    @Test
    fun testEveryNDaysStrict() {
        val behavior = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(3))
            .build().everyNDaysBehavior()

        val everyNDays = Do (name="Test 2", periodBehavior = behavior ,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        everyNDays.id = doManager.addNewDo(everyNDays)

        val settingManager = DaggerAppComponent.builder().build().settingsManager()

        settingManager.setSettingValue(Setting.EVERY_N_DAYS_STRICT, 1)

        val isOblBeforeStart = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 3,1))
        val isAvailBeforeStart = everyNDays.isAvailableOnDate(LocalDate.of(2019, 3, 1))
        val isOblOnEnd = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 12,31))
        val isAvailOnEnd = everyNDays.isAvailableOnDate(LocalDate.of(2019, 12,31))
        val isOblAfterEnd = everyNDays.isObligatoryOnDate(LocalDate.of(2021, 12,31))
        val isAvailAfterEnd = everyNDays.isAvailableOnDate(LocalDate.of(2021, 12,31))

        val isAvailOnFirst = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,1))
        val isOblOnFirst = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,1))
        val isAvailOnSec = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,2))
        val isOblOnSec = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,2))

        val result0 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 2),
            resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result0)

        val isAvailOnThird = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,3))
        val isOblOnThird = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,3))

        val result1 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 3),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result1)

        val isAvailOnFour = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,4))
        val isOblOnFour = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,4))
        val isAvailOnFive = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,5))
        val isOblOnFive = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,5))
        val isAvailOnSix = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,6))
        val isOblOnSix = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,6))

        val result2 = Result(doId = everyNDays.id, date = LocalDate.of(2019, 4, 6),
            resultType = Result.ResultType.SPECIAL_DAY, isPositive = false, wpPoint = 3, chainPoint = 1)
        resultMutator.addResult(result2)

        val isAvailOnResultDay = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,6))
        val isOblOnResultDay = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,6))
        val isAvailOnSev = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,7))
        val isOblOnSev = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,7))
        val isAvailOnEight = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,8))
        val isOblOnEight = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,8))
        val isAvailOnNine = everyNDays.isAvailableOnDate(LocalDate.of(2019, 4,9))
        val isOblOnNine = everyNDays.isObligatoryOnDate(LocalDate.of(2019, 4,9))

        assertThat(isAvailBeforeStart).isFalse()
        assertThat(isOblBeforeStart).isFalse()
        assertThat(isOblOnEnd).isTrue()
        assertThat(isAvailOnEnd).isTrue()
        assertThat(isAvailAfterEnd).isFalse()
        assertThat(isOblAfterEnd).isFalse()
        assertThat(isOblOnResultDay).isFalse()
        assertThat(isAvailOnResultDay).isFalse()

        assertThat(isAvailOnFirst).isTrue()
        assertThat(isOblOnFirst).isFalse()
        assertThat(isAvailOnSec).isTrue()
        assertThat(isOblOnSec).isFalse()
        assertThat(isAvailOnThird).isTrue()
        assertThat(isOblOnThird).isTrue()
        assertThat(isAvailOnFour).isFalse()
        assertThat(isOblOnFour).isFalse()
        assertThat(isAvailOnFive).isFalse()
        assertThat(isOblOnFive).isFalse()
        assertThat(isAvailOnSix).isTrue()
        assertThat(isOblOnSix).isTrue()
        assertThat(isAvailOnSev).isFalse()
        assertThat(isOblOnSev).isFalse()
        assertThat(isAvailOnEight).isFalse()
        assertThat(isOblOnEight).isFalse()
        assertThat(isAvailOnNine).isTrue()
        assertThat(isOblOnNine).isTrue()
    }

    @Test
    fun resultLoaderTest(){
        val loader = ResultLoaderImp()

        val result1 = Result(doId = 10, date = LocalDate.of(2019, 4, 3),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 5, chainPoint = 1)
        resultMutator.addResult(result1)

        val result2 = Result(doId = 10, date = LocalDate.of(2019, 4, 5),
            resultType = Result.ResultType.DONE, isPositive = false, wpPoint = 3, chainPoint = 1)
        resultMutator.addResult(result2)

        val result = loader.getLastResult(10, Result.ResultType.DEFAULT)

        assertThat(result).isEqualTo(result2)
    }
}