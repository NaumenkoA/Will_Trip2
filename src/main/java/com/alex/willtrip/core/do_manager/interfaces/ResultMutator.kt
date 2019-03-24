package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Result
import java.time.LocalDate


interface ResultMutator {
    fun addResult (result: Result)
    fun removeResult (result: Result)
    fun removeResult (doLink: Int, date: LocalDate)
    fun removeResult (doName: String, date: LocalDate)
}