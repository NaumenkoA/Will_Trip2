package com.alex.willtrip.core.result.interfaces

import java.time.LocalDate

interface SkippedResultsChecker {
    fun checkSkippedResults(checkDate: LocalDate): Triple <LocalDate?, LocalDate?, Int?>
}