package com.alex.willtrip.core.result.interfaces

import com.alex.willtrip.core.result.Result
import org.threeten.bp.LocalDate

interface ResultLoader {
    fun loadResultsForPeriod (doId: Long, startDate: LocalDate, endDate: LocalDate, exceptType: Result.ResultType): List <Result>?
    fun loadResultForDate (doId: Long, date: LocalDate): Result?
    fun getNLastResults (doId: Long, numberOfResults: Int, exceptType: Result.ResultType): List <Result>
    fun getLastResult (doId: Long, exceptType: Result.ResultType): Result?

}