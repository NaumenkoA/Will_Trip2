package com.alex.willtrip.core.do_manager

import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.di.*
import com.alex.willtrip.objectbox.class_boxes.DoDB
import com.google.common.truth.Truth.assertThat
import io.objectbox.reactive.DataObserver
import org.junit.Before
import org.junit.Test

import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class DoManagerTest:AbstractObjectBoxTest() {

    lateinit var doManager: DoManager

    @Before
    fun setUp() {
        doManager = DaggerDoManagerComponent.create().doManager()
    }

    @Test
    fun checkDoSaveAndLoad() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 3), expireDate = LocalDate.of(2019, 12, 31))

        val everyNDays = DaggerEveryNDaysBehaviorComponent.builder().settingsComponent(DaggerSettingsComponent.create()).
            everyNDaysBehaviorModule(EveryNDaysBehaviorModule(3))
            .build().everyNDaysBehavior()
        val doObj2 = Do (name="Test 2", periodBehavior = everyNDays ,
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

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        val loaded = doManager.getAllDo()

        assertThat(doObj1).isEqualTo(loaded?.get(0))
        assertThat(doObj2).isEqualTo(loaded?.get(1))
        assertThat(doObj3).isEqualTo(loaded?.get(2))
        assertThat(doObj4).isEqualTo(loaded?.get(3))
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkDoNameExistsThrowsException() {
        val doObj1 = Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 3), expireDate = LocalDate.of(2019, 12, 31))

        doManager.addNewDo(doObj1)

        val doObj2= Do (name="Test", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do2", isSpecialDayEnabled = false, isPositive = false, complexity = 2,
            startDate = LocalDate.of(2019, 4, 3), expireDate = LocalDate.of(2019, 12, 31))

        doManager.addNewDo(doObj2)
    }

    @Test
    fun loadActualDoForDate() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 4, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 5, 3), expireDate = LocalDate.of(2019, 4, 1))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 4, 1))

        val doObj5 = Do (name="Test5", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 5, 1))

        val doObj6 = Do (name="Test6", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 5, 1), expireDate = LocalDate.of(2019, 12, 12))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)
        doManager.addNewDo(doObj5)
        doManager.addNewDo(doObj6)

        val actualList = doManager.getActualDoForDate(LocalDate.of(2019, 4, 1))

        assertThat(actualList).isEqualTo(listOf(doObj2, doObj3, doObj4, doObj6))
    }

    @Test
    fun loadAllExceptArchive() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        val list = doManager.getAllExceptArchive(LocalDate.of(2019, 4,1))

        assertThat(list).isEqualTo(listOf(doObj2, doObj4))
    }

    @Test
    fun loadArchiveDoForDate() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 4, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 5, 3), expireDate = LocalDate.of(2019, 4, 1))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 4, 1))

        val doObj5 = Do (name="Test5", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 5, 1))

        val doObj6 = Do (name="Test6", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 5, 1), expireDate = LocalDate.of(2019, 12, 12))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)
        doManager.addNewDo(doObj5)
        doManager.addNewDo(doObj6)

        val list = doManager.getArchiveDoForDate(LocalDate.of(2019, 4,1))

        assertThat(list).isEqualTo(listOf(doObj1))
    }

    @Test
    fun getIdByName() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 4, 3), expireDate = LocalDate.of(2019, 1, 31))

        doManager.addNewDo(doObj1)

        val id1 = doManager.getIdByName("Test1")
        val id2 = doManager.getIdByName("None")

        assertThat(id1).isEqualTo(1)
        assertThat(id2).isEqualTo(null)
    }

    @Test
    fun removeDoById() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        val id = doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        doManager.removeDo(id)

        val list = doManager.getAllDo()

        assertThat(list).isEqualTo(listOf(doObj1, doObj3, doObj4))
    }

    @Test (expected = IllegalArgumentException::class)
    fun removeDoByWrongIdThrowsException() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)

        doManager.removeDo(100)
    }

    @Test
    fun removeDoByName() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        doManager.removeDo("Test2")

        val list = doManager.getAllDo()

        assertThat(list).isEqualTo(listOf(doObj1, doObj3, doObj4))
    }

    @Test (expected = IllegalArgumentException::class)
    fun removeDoByWrongNameThrowsException() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        doManager.removeDo("Test6")
    }

    @Test
    fun editDoById() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = true, complexity = 5,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)

        val id = doManager.getIdByName("Test1")
        if (id!= null) doManager.editDo(id, doObj4)

        assertThat(doManager.getDoByName("Test1")).isEqualTo(doObj4)
    }

    @Test (expected = IllegalArgumentException::class)
    fun editDoByWrongIdThrowsException() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = true, complexity = 5,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)

        doManager.editDo(100, doObj4)
    }

    @Test
    fun editDoByName() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = true, complexity = 5,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)

        doManager.editDo("Test1", doObj4)

        assertThat(doManager.getDoByName("Test1")).isEqualTo(doObj4)
    }

    @Test (expected = IllegalArgumentException::class)
    fun editDoByWrongNameThrowsException() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = true, complexity = 5,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)

        doManager.editDo("Test5", doObj4)
    }

    @Test
    fun getDoByName () {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = true, complexity = 5,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        assertThat(doManager.getDoByName("Test2")).isEqualTo(doObj2)
        assertThat(doManager.getDoByName("Test6")).isEqualTo(null)
    }

    @Test
    fun getDoById () {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val doObj4 = Do (name="Test4", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = true, complexity = 5,
            startDate = LocalDate.of(2019, 4, 20))

        doManager.addNewDo(doObj1)
        val id = doManager.addNewDo(doObj2)
        doManager.addNewDo(doObj3)
        doManager.addNewDo(doObj4)

        assertThat(doManager.getDoById(id)).isEqualTo(doObj2)
        assertThat(doManager.getDoById(100)).isEqualTo(null)
    }

    @Test
    fun subscriptionTest() {
        val doObj1 = Do (name="Test1", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2018, 3, 3), expireDate = LocalDate.of(2019, 3, 31))

        val doObj2 = Do (name="Test2", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 4, 1), expireDate = LocalDate.of(2019, 12, 31))

        val doObj3 = Do (name="Test3", periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
            note = "Test do", isSpecialDayEnabled = true, isPositive = false, complexity = 4,
            startDate = LocalDate.of(2019, 1, 1), expireDate = LocalDate.of(2019, 3, 31))

        val observer = DoChangeObserver()
        val subscriber = doManager.addObserver(observer)
        doManager.addNewDo(doObj1)

        Thread.sleep(500)

        doManager.removeObserver(subscriber)
        doManager.addNewDo(doObj3)

        assertThat(observer.numOfChanges).isEqualTo(2)
    }

    class DoChangeObserver: DataObserver<Class<DoDB>> {

        var numOfChanges = 0

        override fun onData(data: Class<DoDB>) {
            numOfChanges++
        }
    }
}