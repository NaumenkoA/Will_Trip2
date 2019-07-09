package com.alex.willtrip.core.gratitude

import android.net.Uri
import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.core.willpower.WPManager
import com.alex.willtrip.di.DaggerAppComponent
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import io.objectbox.reactive.DataObserver
import org.junit.Assert.*
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class GratitudeManagerTest: AbstractObjectBoxTest() {

    private lateinit var gratitudeManager: GratitudeManager
    private lateinit var wpManager: WPManager
    private val uri = Uri.EMPTY

    @Before
    fun setUp() {
        gratitudeManager = DaggerAppComponent.builder().build().gratitudeManager()
        wpManager = DaggerAppComponent.builder().build().wpManager()
    }

    @Test
    fun loadGratitudeForDate() {
        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)
        val gratitude4 = Gratitude(date = LocalDate.of(2019, 7, 6), text = "what a super nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)
        gratitudeManager.addGratitude(gratitude4)

        val gratitudeList = gratitudeManager.loadGratitudeForDate(LocalDate.of(2019, 7, 7))
        val gratitudeList1 = gratitudeManager.loadGratitudeForDate(LocalDate.of(2019, 7, 5))

        assertThat(gratitudeList).isEqualTo(listOf(gratitude1, gratitude2, gratitude3))
        assertThat(gratitudeList1).isEmpty()
    }

    @Test
    fun countAllGratitude() {
        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)
        val gratitude4 = Gratitude(date = LocalDate.of(2019, 7, 6), text = "what a super nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)
        gratitudeManager.addGratitude(gratitude4)

        val count = gratitudeManager.countAllGratitude()

        assertThat(count).isEqualTo(4)
    }

    @Test
    fun getGratitudeId() {
        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)
        val gratitude4 = Gratitude(date = LocalDate.of(2019, 7, 6), text = "what a super nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)
        gratitudeManager.addGratitude(gratitude4)

        val id = gratitudeManager.getGratitudeId("what a super nice day!", LocalDate.of(2019, 7, 7))

        assertThat(id).isEqualTo(3)
    }


    @Test
    fun checkGratitudeAlreadyExists() {

        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)

        val t = gratitudeManager.checkGratitudeAlreadyExists(gratitude1)
        val f = gratitudeManager.checkGratitudeAlreadyExists(gratitude3)

        assertThat(t).isTrue()
        assertThat(f).isFalse()
    }

    @Test
    fun editGratitude() {
        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)

        val id = gratitudeManager.getGratitudeId("what a great nice day!", LocalDate.of(2019, 7, 7))
        val gratitude3 = Gratitude(id, LocalDate.of(2019, 7, 7), "what a nice beautiful rain!", uri)

        gratitudeManager.editGratitude(gratitude3)
        val newGratitude = gratitudeManager.getGratitudeById(id)

        assertThat(newGratitude).isEqualTo(gratitude3)
    }

    @Test (expected = IllegalArgumentException::class)
    fun editGratitudeThrowsException() {
        val gratitude1 = Gratitude(id=10, date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        gratitudeManager.editGratitude(gratitude1)
    }

    @Test (expected = IllegalArgumentException::class)
    fun addSameGratitudeThrowsException() {
        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)
    }

    @Test
    fun addGratitudeBonusCheck() {
        wpManager.increaseWP(100)
        val startingWP = wpManager.getCurrentWP()

        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)
        val gratitude4 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super pupper nice day!", photoUri = uri)
        val gratitude5 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super sexy nice day!", photoUri = uri)
        val gratitude6 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super smoky nice day!", photoUri = uri)
        val gratitude7 = Gratitude(date = LocalDate.of(2019, 7, 8), text = "what a super smoky nice day!", photoUri = uri)
        val gratitude8 = Gratitude(date = LocalDate.of(2019, 7, 8), text = "what a super nice day!", photoUri = uri)
        val gratitude9 = Gratitude(date = LocalDate.of(2019, 7, 8), text = "what a  nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)
        gratitudeManager.addGratitude(gratitude4)
        gratitudeManager.addGratitude(gratitude5)
        gratitudeManager.addGratitude(gratitude6)
        gratitudeManager.addGratitude(gratitude7)
        gratitudeManager.addGratitude(gratitude8)
        gratitudeManager.addGratitude(gratitude9)

        val endingWP = wpManager.getCurrentWP()

        assertThat(endingWP - startingWP).isEqualTo(6)
    }

    @Test (expected = IllegalStateException::class)
    fun removeGratitudeWithNotExistingIdThrowsException() {
        gratitudeManager.removeGratitude(1)
    }

    @Test
    fun removeGratitudeWPDecreaseCheck() {
        wpManager.increaseWP(100)

        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)
        val gratitude4 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super pupper nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)
        gratitudeManager.addGratitude(gratitude4)

        val midValue = wpManager.getCurrentWP()

        gratitudeManager.removeGratitude(1)
        val midValue1 = wpManager.getCurrentWP()

        gratitudeManager.removeGratitude(2)
        val endValue = wpManager.getCurrentWP()

        gratitudeManager.removeGratitude(3)
        gratitudeManager.removeGratitude(4)
        val endValue1 = wpManager.getCurrentWP()

        assertThat(midValue).isEqualTo(103)
        assertThat(midValue1).isEqualTo(103)
        assertThat(endValue).isEqualTo(100)
        assertThat(endValue1).isEqualTo(100)
    }

    @Test
    fun checkGratitudeObserver() {
        val observer = GratitudeObserver()
        val subscriber = gratitudeManager.addGratitudeObserver(LocalDate.of(2019, 7, 7), observer)

        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)

        gratitudeManager.removeGratitude(2)

        Thread.sleep(500)

        gratitudeManager.removeGratitudeObserver(subscriber)
        gratitudeManager.removeGratitude(1)

        assertThat (observer.list).isEqualTo(listOf(gratitude1, gratitude3))
    }

    @Test
    fun checkGratitudeWPBonusObserver() {

        val observer = WPBonusObserver()
        val subscriber = gratitudeManager.addWPBonusObserver(LocalDate.of(2019, 7, 7), observer)

        val gratitude1 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a nice day!", photoUri = uri)
        val gratitude2 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a great nice day!", photoUri = uri)
        val gratitude3 = Gratitude(date = LocalDate.of(2019, 7, 7), text = "what a super nice day!", photoUri = uri)

        gratitudeManager.addGratitude(gratitude1)
        gratitudeManager.addGratitude(gratitude2)
        gratitudeManager.addGratitude(gratitude3)

        Thread.sleep(500)

        gratitudeManager.removeWPBonusObserver(subscriber)
        gratitudeManager.removeGratitude(1)

        assertThat (observer.isBonusExists).isEqualTo(true)
    }

    class WPBonusObserver: DataObserver<Boolean> {
        var isBonusExists: Boolean = false

        override fun onData(data: Boolean) {
            isBonusExists = data
        }
    }

    class GratitudeObserver: DataObserver <List<Gratitude>> {

        var list = emptyList<Gratitude>()

        override fun onData(data: List<Gratitude>) {
            list = data
        }
    }
}