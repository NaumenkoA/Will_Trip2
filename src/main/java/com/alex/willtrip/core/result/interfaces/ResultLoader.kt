package com.alex.willtrip.core.result.interfaces

import com.alex.willtrip.core.result.Result
import org.threeten.bp.LocalDate

interface ResultLoader {
    fun loadResultsForPeriod (doId: Long, startDate: LocalDate, endDate: LocalDate): List <Result>?
    fun loadResultForDate (doId: Long, date: LocalDate): Result?
    fun getLastResults (doId: Long, numberOfResults: Int): List <Result>
    fun getLastNonSkippedResults(doId: Long, numberOfResults: Int): List <Result>
}