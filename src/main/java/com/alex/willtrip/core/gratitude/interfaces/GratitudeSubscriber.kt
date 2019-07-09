package com.alex.willtrip.core.gratitude.interfaces

import com.alex.willtrip.core.gratitude.Gratitude
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate

interface GratitudeSubscriber {
    fun addObserver (date: LocalDate, observer: DataObserver<List<Gratitude>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}