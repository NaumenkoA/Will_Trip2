package com.alex.willtrip.core.skipped

import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.settings.SettingsManager
import com.alex.willtrip.di.*
import org.junit.Test
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.threeten.bp.LocalDate

class SkippedResultsManagerTest: AbstractObjectBoxTest() {

    lateinit var skippedManager: SkippedResultsManager
    lateinit var doManager: DoManager
    lateinit var resultManager: ResultManager
    lateinit var settingManager: SettingsManager

    @Before
    fun setUp() {
        skippedManager = DaggerAppComponent.builder().build().skippedResultsManager()
        doManager = DaggerAppComponent.builder().build().doManager()
        resultManager = DaggerAppComponent.builder().build().resultManager()
        settingManager = DaggerAppComponent.builder().build().settingsManager()
    }

    @Test
    fun checkHasSkippedResultsEveryDay() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 10))

        val id = doManager.addNewDo(doObj1)

        val t1 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 1))
        val t = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 15))
        val f = skippedManager.checkHasSkippedResults(id, LocalDate.of(2018, 12, 31))

        assertThat(f).isFalse()
        assertThat(t).isTrue()
        assertThat(t1).isTrue()

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 2), isPositive = false, wpPoint = 4, chainPoint = 0)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        val f2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 2))
        val t2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 3))
        val t3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 15))

        assertThat(f2).isFalse()
        assertThat(t2).isTrue()
        assertThat(t3).isTrue()
        }

    @Test
    fun checkHasSkippedResultsDaysOfWeek() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerDaysOfWeekBehaviorComponent.builder().
            daysOfWeekBehaviorModule(DaysOfWeekBehaviorModule(listOf(1,4,7))).build().daysOfWeekBehavior(),
            note = "Test do3", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 15))

        val id = doManager.addNewDo(doObj1)

        val f1 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 1))
        val t = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 3))
        val t1 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 20))
        val f = skippedManager.checkHasSkippedResults(id, LocalDate.of(2018, 12, 31))

        assertThat(f).isFalse()
        assertThat(t).isTrue()
        assertThat(t1).isTrue()
        assertThat(f1).isFalse()

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 3), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 6), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 7), isPositive = false, wpPoint = 4, chainPoint = 0)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)

        val f2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 9))
        val t2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 10))
        val t3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 15))

        assertThat(f2).isFalse()
        assertThat(t2).isTrue()
        assertThat(t3).isTrue()
    }

    @Test
    fun checkHasSkippedResultsNDaysAWeek() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerNTimesAWeekBehaviorComponent.builder().
            nTimesAWeekBehaviorModule(NTimesAWeekBehaviorModule(3)).build().nTimesAWeekBehavior(),
            note = "Test do4", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 15))

        val id = doManager.addNewDo(doObj1)

        val t1 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 1))
        val t = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 20))
        val f = skippedManager.checkHasSkippedResults(id, LocalDate.of(2018, 12, 31))

        assertThat(f).isFalse()
        assertThat(t).isTrue()
        assertThat(t1).isTrue()

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 3), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 5), isPositive = false, wpPoint = 4, chainPoint = 0)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        val t2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 4))

        resultManager.addResult(result3)

        val t3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 7))
        val f2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 6))

        assertThat(t2).isTrue()
        assertThat(t3).isTrue()
        assertThat(f2).isFalse()
    }

    @Test
    fun checkHasSkippedResultsEveryNDaysNotStrict() {
        val everyNDays = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(4)).build().everyNDaysBehavior()
        val doObj1 = Do (name="Test", periodBehavior = everyNDays,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 15))

        val id = doManager.addNewDo(doObj1)

        val t = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 1))
        val t1 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 3))
        val t2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 20))
        val f = skippedManager.checkHasSkippedResults(id, LocalDate.of(2018, 12, 31))

        assertThat(t1).isTrue()
        assertThat(t).isTrue()
        assertThat(t2).isTrue()
        assertThat(f).isFalse()

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 3), isPositive = false, wpPoint = 4, chainPoint = 0)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        val t3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 4))
        val f2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 3))
        val f3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 2))
        val t4 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 5))
        val t5 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 10))

        assertThat(t3).isTrue()
        assertThat(f2).isFalse()
        assertThat(f3).isFalse()
        assertThat(t4).isTrue()
        assertThat(t5).isTrue()
    }

    @Test
    fun checkHasSkippedResultsEveryNDaysStrict() {
        settingManager.setSettingValue(Setting.EVERY_N_DAYS_STRICT, 1)

        val everyNDays = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(4)).build().everyNDaysBehavior()
        val doObj1 = Do (name="Test", periodBehavior = everyNDays,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 15))

        val id = doManager.addNewDo(doObj1)

        val t = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 1))
        val t1 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 3))
        val t2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 20))
        val f = skippedManager.checkHasSkippedResults(id, LocalDate.of(2018, 12, 31))

        assertThat(t1).isTrue()
        assertThat(t).isTrue()
        assertThat(t2).isTrue()
        assertThat(f).isFalse()

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 5), isPositive = false, wpPoint = 4, chainPoint = 0)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        val f2 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 4))
        val f3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 5))
        val f4 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 7))
        val f5 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 8))
        val t3 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 9))
        val t4 = skippedManager.checkHasSkippedResults(id, LocalDate.of(2019, 1, 10))

        assertThat(t3).isTrue()
        assertThat(f2).isFalse()
        assertThat(f3).isFalse()
        assertThat(f4).isFalse()
        assertThat(f5).isFalse()
        assertThat(t4).isTrue()
    }

    @Test
    fun fillInSkippedResultsEveryDay() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 10))

        val id = doManager.addNewDo(doObj1)

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 2), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 3), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result4 = Result(doId = id, date = LocalDate.of(2019, 1, 4), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result5 = Result(doId = id, date = LocalDate.of(2019, 1, 5), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result6 = Result(doId = id, date = LocalDate.of(2019, 1, 6), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result7 = Result(doId = id, date = LocalDate.of(2019, 1, 7), isPositive = true, wpPoint = 4, chainPoint = 2)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)
        resultManager.addResult(result7)

        val skipped = skippedManager.fillInSkippedResults(LocalDate.of(2019, 1, 10))
        assertThat(skipped).isEqualTo(Skipped(LocalDate.of(2019, 1, 8),
            LocalDate.of(2019,1,10), 3, 14))

        val f = skippedManager.checkHasSkippedResults(id,LocalDate.of(2019, 1, 10))

        assertThat(f).isFalse()
    }

    @Test
    fun fillInSkippedResultsDaysOfWeek() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerDaysOfWeekBehaviorComponent.builder().
            daysOfWeekBehaviorModule(DaysOfWeekBehaviorModule(listOf(1,4,7))).build().daysOfWeekBehavior(),
            note = "Test do3", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 30))

        val id = doManager.addNewDo(doObj1)

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 3), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 6), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 7), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result4 = Result(doId = id, date = LocalDate.of(2019, 1, 10), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result5 = Result(doId = id, date = LocalDate.of(2019, 1, 13), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result6 = Result(doId = id, date = LocalDate.of(2019, 1, 14), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result7 = Result(doId = id, date = LocalDate.of(2019, 1, 17), isPositive = true, wpPoint = 4, chainPoint = 2)
        val result8 = Result(doId = id, date = LocalDate.of(2019, 1, 20), isPositive = true, wpPoint = 4, chainPoint = 2)
        val result9 = Result(doId = id, date = LocalDate.of(2019, 1, 21), isPositive = true, wpPoint = 4, chainPoint = 3)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)
        resultManager.addResult(result7)
        resultManager.addResult(result8)
        resultManager.addResult(result9)

        val skipped = skippedManager.fillInSkippedResults(LocalDate.of(2019, 1, 30))
        assertThat(skipped).isEqualTo(Skipped(LocalDate.of(2019, 1, 24),
            LocalDate.of(2019,1,28), 3, 15))

        val f = skippedManager.checkHasSkippedResults(id,LocalDate.of(2019,1,30))

        assertThat(f).isFalse()
    }

    @Test
    fun fillInSkippedResultsNTimesAWeek() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerNTimesAWeekBehaviorComponent.builder().
            nTimesAWeekBehaviorModule(NTimesAWeekBehaviorModule(3)).build().nTimesAWeekBehavior(),
            note = "Test do4", isSpecialDayEnabled = false, isPositive = false, complexity = 5,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 30))

        val id = doManager.addNewDo(doObj1)

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 3), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 5), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 6), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result4 = Result(doId = id, date = LocalDate.of(2019, 1, 8), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result5 = Result(doId = id, date = LocalDate.of(2019, 1, 10), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result6 = Result(doId = id, date = LocalDate.of(2019, 1, 13), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result7 = Result(doId = id, date = LocalDate.of(2019, 1, 16), isPositive = true, wpPoint = 4, chainPoint = 2)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)
        resultManager.addResult(result7)

        val skipped = skippedManager.fillInSkippedResults(LocalDate.of(2019, 1, 25))
        assertThat(skipped).isEqualTo(Skipped(LocalDate.of(2019, 1, 19),
            LocalDate.of(2019,1,25), 3, 17))

        val f = skippedManager.checkHasSkippedResults(id,LocalDate.of(2019,1,25))

        assertThat(f).isFalse()
    }

    @Test
    fun fillInEveryNDaysStrict(){
        val everyNDays = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(4)).build().everyNDaysBehavior()
        val doObj1 = Do (name="Test", periodBehavior = everyNDays,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 1, 31))

        val id = doManager.addNewDo(doObj1)

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 4), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 7), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result4 = Result(doId = id, date = LocalDate.of(2019, 1, 9), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result5 = Result(doId = id, date = LocalDate.of(2019, 1, 12), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result6 = Result(doId = id, date = LocalDate.of(2019, 1, 15), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result7 = Result(doId = id, date = LocalDate.of(2019, 1, 19), isPositive = true, wpPoint = 4, chainPoint = 2)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)
        resultManager.addResult(result7)

        val skipped = skippedManager.fillInSkippedResults(LocalDate.of(2019, 1, 31))

        assertThat(skipped).isEqualTo(Skipped(LocalDate.of(2019, 1, 23),
            LocalDate.of(2019,1,31), 3, 11))

        val f = skippedManager.checkHasSkippedResults(id,LocalDate.of(2019,1,31))

        assertThat(f).isFalse()
    }

    @Test
    fun fillInEveryNDaysNotStrict(){
        settingManager.setSettingValue(Setting.EVERY_N_DAYS_STRICT, 1)

        val everyNDays = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(4)).build().everyNDaysBehavior()
        val doObj1 = Do (name="Test", periodBehavior = everyNDays,
            note = "Test do2", isSpecialDayEnabled = false, isPositive = true, complexity = 3,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 2, 20))

        val id = doManager.addNewDo(doObj1)

        val result1 = Result(doId = id, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id, date = LocalDate.of(2019, 1, 5), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id, date = LocalDate.of(2019, 1, 9), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result4 = Result(doId = id, date = LocalDate.of(2019, 1, 13), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result5 = Result(doId = id, date = LocalDate.of(2019, 1, 17), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result6 = Result(doId = id, date = LocalDate.of(2019, 1, 21), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result7 = Result(doId = id, date = LocalDate.of(2019, 1, 25), isPositive = true, wpPoint = 4, chainPoint = 2)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)
        resultManager.addResult(result7)

        val skipped = skippedManager.fillInSkippedResults(LocalDate.of(2019, 2, 10))

        assertThat(skipped).isEqualTo(Skipped(LocalDate.of(2019, 1, 29),
            LocalDate.of(2019,2,10), 4, 14))

        val f = skippedManager.checkHasSkippedResults(id,LocalDate.of(2019,2,10))

        assertThat(f).isFalse()
    }

    @Test
    fun fillInSkippedManyDo(){
        val doObj1 = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 1,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 2, 28))

        val doObj2 = Do (name="Test1", periodBehavior = DaggerNTimesAWeekBehaviorComponent.builder().
            nTimesAWeekBehaviorModule(NTimesAWeekBehaviorModule(3)).build().nTimesAWeekBehavior(),
            note = "Test do4", isSpecialDayEnabled = false, isPositive = false, complexity = 2,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 2, 28))

        val doObj3 = Do (name="Test2", periodBehavior = DaggerDaysOfWeekBehaviorComponent.builder().
            daysOfWeekBehaviorModule(DaysOfWeekBehaviorModule(listOf(1,4,7))).build().daysOfWeekBehavior(),
            note = "Test do3", isSpecialDayEnabled = true, isPositive = false, complexity = 3,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 2, 28))

        val id1 = doManager.addNewDo(doObj1)
        val id2 = doManager.addNewDo(doObj2)
        val id3 = doManager.addNewDo(doObj3)

        val result1 = Result(doId = id1, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result2 = Result(doId = id1, date = LocalDate.of(2019, 1, 2), isPositive = false, wpPoint = 4, chainPoint = 0)
        val result3 = Result(doId = id2, date = LocalDate.of(2019, 1, 1), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result4 = Result(doId = id2, date = LocalDate.of(2019, 1, 4), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result5 = Result(doId = id3, date = LocalDate.of(2019, 1, 3), isPositive = true, wpPoint = 4, chainPoint = 0)
        val result6 = Result(doId = id3, date = LocalDate.of(2019, 1, 6), isPositive = true, wpPoint = 4, chainPoint = 0)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)

        val skipped = skippedManager.fillInSkippedResults(LocalDate.of(2019, 1, 13))

        assertThat(skipped).isEqualTo(Skipped(LocalDate.of(2019, 1, 3),
            LocalDate.of(2019,1,13), 18, 28))

        val f = skippedManager.checkHasSkippedResults(id1,LocalDate.of(2019,1,13))
        val f2 = skippedManager.checkHasSkippedResults(id2,LocalDate.of(2019,1,13))
        val f3 = skippedManager.checkHasSkippedResults(id3,LocalDate.of(2019,1,13))

        assertThat(f&&f2&&f3).isFalse()

        val skipped1 = skippedManager.fillInSkippedResults(LocalDate.of(2019, 1, 20))

        assertThat(skipped1).isEqualTo(Skipped(LocalDate.of(2019, 1, 14),
            LocalDate.of(2019,1,20), 13, 22))

        val f4 = skippedManager.checkHasSkippedResults(id1,LocalDate.of(2019,1,20))
        val f5 = skippedManager.checkHasSkippedResults(id2,LocalDate.of(2019,1,20))
        val f6 = skippedManager.checkHasSkippedResults(id3,LocalDate.of(2019,1,20))

        assertThat(f4&&f5&&f6).isFalse()
    }
    }

