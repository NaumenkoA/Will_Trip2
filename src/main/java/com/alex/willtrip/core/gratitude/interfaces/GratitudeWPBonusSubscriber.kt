package com.alex.willtrip.core.gratitude.interfaces

import com.alex.willtrip.core.gratitude.Gratitude
import com.alex.willtrip.core.gratitude.GratitudeWPBonus
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate

interface GratitudeWPBonusSubscriber {
    fun addObserver (date: LocalDate, observer: DataObserver<Boolean>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}