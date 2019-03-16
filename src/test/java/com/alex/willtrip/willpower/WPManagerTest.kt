package com.alex.willtrip.willpower
import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.di.DaggerWPComponent
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import io.objectbox.reactive.DataObserver
import org.junit.Before

class WPManagerTest: AbstractObjectBoxTest() {


        lateinit var wpManager: WPManager

        @Before
        fun setUp() {
            wpManager = DaggerWPComponent.create().getWPManager()
        }


    @Test
    fun checkLoadWhenDBEmpty() {
        assertThat(wpManager.getCurrentWP()).isEqualTo(0)
    }

    @Test
    fun saveAndLoadWP() {
        val initialValue = wpManager.getCurrentWP()
        wpManager.decreaseWP(initialValue)
        wpManager.increaseWP(50)
        wpManager.decreaseWP(30)
        val dbValue = wpManager.loader.loadWillPower()
        assertThat(wpManager.getCurrentWP()).isEqualTo(dbValue.willPower)
        assertThat(wpManager.getCurrentWP()).isEqualTo(20)
    }

    @Test
    fun increaseWP() {
        val currentWP = wpManager.getCurrentWP()
        val newWP = wpManager.increaseWP(5)
        assertThat(newWP).isEqualTo(currentWP + 5)
    }

    @Test
    fun decreaseWP() {
        val currentWP = wpManager.increaseWP(100)
        val newWP = wpManager.decreaseWP(10)
        assertThat(newWP).isEqualTo(currentWP - 10)
    }

    @Test
    fun checkWPNotLessThanZero() {
        val currentWP = wpManager.increaseWP(10)
        val newWP = wpManager.decreaseWP(1000)
        assertThat(newWP).isEqualTo(0)
    }

    @Test
    fun checkWPSubscribing() {
        val observer = WPChangeObserver()
        val subscriber = wpManager.addWPObserver(observer)
        val value = wpManager.increaseWP(30)

        Thread.sleep(500)

        wpManager.removeWPObserver(subscriber)
        wpManager.increaseWP(20)

        assertThat (observer.wpNewValue).isEqualTo(value)
    }

    class WPChangeObserver: DataObserver <Int> {
        var wpNewValue: Int = 0

        override fun onData(data: Int) {
            wpNewValue = data
        }
    }
}