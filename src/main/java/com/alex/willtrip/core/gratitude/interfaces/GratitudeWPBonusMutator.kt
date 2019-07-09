package com.alex.willtrip.core.gratitude.interfaces

import org.threeten.bp.LocalDate

interface GratitudeWPBonusMutator {
    fun addGratitudeWPBonus (date: LocalDate)
    fun removeGratitudeWPBonus (date: LocalDate)
    fun checkGratitudeWPBonusExists (date: LocalDate): Boolean
}