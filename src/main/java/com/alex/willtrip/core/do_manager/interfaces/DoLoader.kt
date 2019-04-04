package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Do
import org.threeten.bp.LocalDate

interface DoLoader {
    fun loadAllDo(): List<Do>?
    fun loadAllExceptArchive(date: LocalDate): List<Do>?
    fun loadActualDoForDate (date: LocalDate): List<Do>?
    fun loadArchiveDoForDate (date:LocalDate): List<Do>?
    fun loadDoById (id: Long): Do?
    fun loadDoByName (name: String): Do?
    fun getIdByName(doName:String): Long?
    fun checkDoNameExists(doName:String): Boolean
}