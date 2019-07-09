package com.alex.willtrip.core.gratitude.implementations

import com.alex.willtrip.core.gratitude.GratitudeWPBonus
import com.alex.willtrip.core.gratitude.GratitudeWPBonus_
import com.alex.willtrip.core.gratitude.interfaces.GratitudeWPBonusMutator
import com.alex.willtrip.core.gratitude.interfaces.GratitudeWPBonusSubscriber
import com.alex.willtrip.objectbox.ObjectBox
import io.objectbox.Box
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class GratitudeWPBonusDB: GratitudeWPBonusMutator, GratitudeWPBonusSubscriber {

    private fun getGratitudeWPBonusBox(): Box<GratitudeWPBonus> {
        return ObjectBox.boxStore.boxFor(GratitudeWPBonus::class.java)
    }

    override fun addGratitudeWPBonus(date: LocalDate) {
        if (checkGratitudeWPBonusExists(date)) throw IllegalArgumentException ("${GratitudeWPBonusDB::class.java.simpleName}: " +
                "can't add new GratitudeWPBonus, GratitudeWPBonus on date $date already exists")

        getGratitudeWPBonusBox().put(GratitudeWPBonus(date = date))

    }

    override fun removeGratitudeWPBonus(date: LocalDate) {
        val gratitudeWPBonus = getGratitudeWPBonusBox().query().equal(GratitudeWPBonus_.date, date.toEpochDay()).
                build().findUnique()

        gratitudeWPBonus?.id?.let { getGratitudeWPBonusBox().remove(it) }
    }

    override fun checkGratitudeWPBonusExists(date: LocalDate): Boolean {
        val gratitudeWPBonus = getGratitudeWPBonusBox().query().equal(GratitudeWPBonus_.date, date.toEpochDay()).
            build().findUnique()

        return gratitudeWPBonus != null
    }

    override fun addObserver(date: LocalDate, observer: DataObserver<Boolean>): DataSubscription {
        val query = getGratitudeWPBonusBox().query().equal(GratitudeWPBonus_.date, date.toEpochDay()).build()
        return query.subscribe().transform {
            val gratitudeWPBonus = query.findUnique()
            gratitudeWPBonus != null
        }.observer(observer)
    }

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }
}