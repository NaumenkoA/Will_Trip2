package com.alex.willtrip.core.do_manager.period

import com.alex.willtrip.core.do_manager.Do
import org.threeten.bp.LocalDate

abstract class PeriodBehavior {

  abstract fun getTypeInfo (): String

  abstract fun isObligatoryOnDate (evaluatedDo: Do, evaluatedDate: LocalDate): Boolean

  abstract fun isAvailableOnDate (evaluatedDo: Do, evaluatedDate: LocalDate): Boolean
}