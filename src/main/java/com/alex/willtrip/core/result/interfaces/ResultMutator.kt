package com.alex.willtrip.core.result.interfaces

import com.alex.willtrip.core.result.Result
import org.threeten.bp.LocalDate


interface ResultMutator {
    fun addResult (result: Result)
    fun removeAllResults (doId: Long)
    fun removeResult (result: Result): Boolean
    fun removeResult (doId: Long, date: LocalDate)
}