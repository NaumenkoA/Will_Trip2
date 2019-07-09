package com.alex.willtrip.core.gratitude.implementations

import com.alex.willtrip.core.gratitude.Gratitude
import com.alex.willtrip.core.gratitude.Gratitude_
import com.alex.willtrip.core.gratitude.Gratitude_.text
import com.alex.willtrip.core.gratitude.interfaces.GratitudeLoader
import com.alex.willtrip.core.gratitude.interfaces.GratitudeMutator
import com.alex.willtrip.core.gratitude.interfaces.GratitudeSubscriber
import com.alex.willtrip.objectbox.ObjectBox
import io.objectbox.Box
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class GratitudeDB: GratitudeLoader, GratitudeMutator, GratitudeSubscriber {

    private fun getGratitudeBox(): Box<Gratitude> {
        return ObjectBox.boxStore.boxFor(Gratitude::class.java)
    }

    override fun getGratitudeById(id: Long): Gratitude {
        return getGratitudeBox().get(id)
    }

    override fun loadGratitudeForDate(date: LocalDate): List<Gratitude> {
        return getGratitudeBox().query().equal(Gratitude_.date, date.toEpochDay()).build().find()
    }

    override fun countGratitudeForDate(date: LocalDate): Int {
        return getGratitudeBox().query().equal(Gratitude_.date, date.toEpochDay()).build().find().size
    }

    override fun countAllGratitude(): Int = getGratitudeBox().count().toInt()


    override fun addGratitude(gratitude: Gratitude) {
        if (checkGratitudeAlreadyExists(gratitude)) throw IllegalArgumentException ("${GratitudeDB::class.java.simpleName}: " +
                "can't add new gratitude, gratitude with $text already exists on date ${gratitude.date}")

        getGratitudeBox().put(gratitude)
    }

    override fun removeGratitude(id: Long) {
        getGratitudeBox().remove(id)
    }

    override fun checkGratitudeAlreadyExists(gratitude: Gratitude): Boolean {
        val gratitudeDB =  getGratitudeBox().query().equal(Gratitude_.text, gratitude.text).
            equal(Gratitude_.date, gratitude.date.toEpochDay()).build().findUnique()

        return gratitudeDB != null
    }

    override fun getGratitudeId(gratitudeText: String, date: LocalDate): Long {
        val gratitude = getGratitudeBox().query().equal(text, gratitudeText).
            equal(Gratitude_.date, date.toEpochDay()).build().findUnique()

        return gratitude?.id ?: 0
    }

    override fun editGratitude(gratitude: Gratitude) {
        val gratitudeDb = getGratitudeBox().get(gratitude.id)

        if (gratitudeDb == null) throw IllegalArgumentException ("${GratitudeDB::class.java.simpleName}: can't edit, " +
                "gratitude with ${gratitude.id} wasn't found")

        getGratitudeBox().put(gratitude)
    }

    override fun addObserver(date: LocalDate, observer: DataObserver<List<Gratitude>>): DataSubscription {
        val query = getGratitudeBox().query().equal(Gratitude_.date, date.toEpochDay()).build()
        return query.subscribe().observer(observer)
    }

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }
}