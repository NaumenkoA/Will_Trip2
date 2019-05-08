package com.alex.willtrip.core.skipped

import org.threeten.bp.LocalDate

interface SkippedResultsChecker {
    fun checkHasSkippedResults(doId: Long, beforeDate: LocalDate): Boolean
    fun fillInSkippedResults(beforeDate: LocalDate): Skipped
}