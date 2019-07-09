package com.alex.willtrip.core.gratitude

import com.alex.willtrip.core.gratitude.interfaces.*
import com.alex.willtrip.core.willpower.WPManager
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate

class GratitudeManager (val wpManager: WPManager, val gratitudeLoader: GratitudeLoader, val gratitudeMutator: GratitudeMutator,
                        val gratitudeSubscriber: GratitudeSubscriber, val gratitudeWPBonusMutator: GratitudeWPBonusMutator,
                        val gratitudeWPBonusSubscriber: GratitudeWPBonusSubscriber) {

    fun loadGratitudeForDate (date: LocalDate): List <Gratitude> = gratitudeLoader.loadGratitudeForDate(date)

    fun countAllGratitude (): Int = gratitudeLoader.countAllGratitude()

    fun getGratitudeId(gratitudeText: String, date: LocalDate):Long = gratitudeLoader.getGratitudeId(gratitudeText, date)

    fun getGratitudeById(id: Long):Gratitude = gratitudeLoader.getGratitudeById(id)

    fun addGratitude (gratitude: Gratitude) {
        gratitudeMutator.addGratitude(gratitude)
        if (gratitudeLoader.countGratitudeForDate(gratitude.date)==3 &&
                    !gratitudeWPBonusMutator.checkGratitudeWPBonusExists(gratitude.date)
                ) {
            gratitudeWPBonusMutator.addGratitudeWPBonus(gratitude.date)
            wpManager.increaseWP(3)
        }
    }

    fun removeGratitude (id:Long){
        val gratitude = gratitudeLoader.getGratitudeById(id)
        gratitudeMutator.removeGratitude(id)

        if (gratitudeLoader.countGratitudeForDate(gratitude.date)<3 &&
            gratitudeWPBonusMutator.checkGratitudeWPBonusExists(gratitude.date)
        ) {
            gratitudeWPBonusMutator.removeGratitudeWPBonus(gratitude.date)
            wpManager.decreaseWP(3)
        }
    }

    fun checkGratitudeAlreadyExists (gratitude: Gratitude): Boolean = gratitudeMutator.checkGratitudeAlreadyExists(gratitude)

    fun editGratitude (gratitude: Gratitude) = gratitudeMutator.editGratitude(gratitude)

    fun addGratitudeObserver (date: LocalDate, observer: DataObserver<List<Gratitude>>): DataSubscription {
        return gratitudeSubscriber.addObserver(date, observer)
    }

    fun removeGratitudeObserver (dataSubscription: DataSubscription) {
        gratitudeSubscriber.removeObserver(dataSubscription)
    }

    fun addWPBonusObserver (date: LocalDate, observer: DataObserver<Boolean>): DataSubscription{
        return gratitudeWPBonusSubscriber.addObserver(date, observer)
    }

    fun removeWPBonusObserver (dataSubscription: DataSubscription) {
        gratitudeWPBonusSubscriber.removeObserver(dataSubscription)
    }
}