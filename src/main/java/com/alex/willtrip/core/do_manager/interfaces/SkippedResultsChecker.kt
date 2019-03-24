package com.alex.willtrip.core.do_manager.interfaces

import java.time.LocalDate

interface SkippedResultsChecker {
    fun checkSkippedResults(beforeDate: LocalDate): Triple <LocalDate, LocalDate, Int>
}