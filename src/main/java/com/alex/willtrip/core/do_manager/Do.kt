package com.alex.willtrip.core.do_manager

import com.alex.willtrip.core.do_manager.interfaces.PeriodBehavior
import io.objectbox.annotation.Id
import org.threeten.bp.LocalDate

class Do (@Id var id: Long = 0,
          val link: Int,
          val name: String,
          private val periodBehavior: PeriodBehavior,
          val note: String?,
          val isPositive: Boolean,
          val complexity: Int,
          val isSpecialDayEnabled: Boolean = false,
          val startDate: LocalDate,
          val expireDate: LocalDate = LocalDate.MAX) {

    fun isObligatoryOnDate (date: LocalDate) = periodBehavior.isObligatoryOnDate(this, date)

    fun isAvailableOnDate (date: LocalDate) = periodBehavior.isAvailableOnDate(this, date)
}