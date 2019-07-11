package com.alex.willtrip.core.do_manager

import com.alex.willtrip.core.do_manager.period.PeriodBehavior
import org.threeten.bp.LocalDate

class Do (var id: Long = 0,
          val name: String,
          val periodBehavior: PeriodBehavior,
          val note: String?,
          val isPositive: Boolean,
          val complexity: Int,
          val isSpecialDayEnabled: Boolean = false,
          val startDate: LocalDate,
          val expireDate: LocalDate = LocalDate.MAX) {

  fun isObligatoryOnDate (date: LocalDate) = periodBehavior.isObligatoryOnDate(this, date)

  fun isAvailableOnDate (date: LocalDate) = periodBehavior.isAvailableOnDate(this, date)

  override fun equals(other: Any?): Boolean {
    if (other !is Do) return false

    return (other.name == name && other.periodBehavior == periodBehavior &&
            other.note == note && other.isPositive == isPositive &&
            other.complexity == complexity && other.isSpecialDayEnabled == isSpecialDayEnabled &&
            other.startDate == startDate && other.expireDate == expireDate)
  }
}