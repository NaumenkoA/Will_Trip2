package com.alex.willtrip.core.gratitude.interfaces

import com.alex.willtrip.core.gratitude.Gratitude
import org.threeten.bp.LocalDate

interface GratitudeLoader {
    fun loadGratitudeForDate (date: LocalDate): List <Gratitude>
    fun countAllGratitude (): Int
    fun countGratitudeForDate (date: LocalDate): Int
    fun getGratitudeById (id: Long): Gratitude
    fun getGratitudeId(gratitudeText: String, date: LocalDate):Long
}