package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Result
import org.threeten.bp.LocalDate

interface ResultLoader {
    fun loadResultsForPeriod (doLink: Int, startDate: LocalDate, endDate: LocalDate): List <Result>?
    fun loadResultForDate (doLink: Int, date: LocalDate): Result?
    fun getLastResults (numberOfResults: Int): List <Result>
}