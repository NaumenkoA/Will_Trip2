package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.interfaces.ResultLoader
import org.threeten.bp.LocalDate

interface PeriodBehavior {
    fun getTypeInfo (): String

    fun isObligatoryOnDate (evaluatedDo: Do, evaluatedDate: LocalDate): Boolean

    fun isAvailableOnDate (evaluatedDo: Do, evaluatedDate: LocalDate): Boolean
}