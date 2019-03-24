package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Do
import java.time.LocalDate

interface DoLoader {
    fun loadAllDo(): List<Do>?
    fun loadActualDoForDate (date:LocalDate): List<Do>?
    fun loadArchiveDoForDate (date:LocalDate): List<Do>?
}