package com.alex.willtrip.core.skipped

import com.alex.willtrip.core.do_manager.period.EveryNDaysBehavior
import org.threeten.bp.LocalDate

class Skipped (val startDate: LocalDate?, val endDate: LocalDate?, val skippedNum: Int, val lostWP: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Skipped) return false
        return (startDate == other.startDate && endDate == other.endDate && skippedNum == other.skippedNum && other.lostWP == lostWP)
    }

    override fun hashCode(): Int {
        var result = startDate?.hashCode() ?: 0
        result = 31 * result + (endDate?.hashCode() ?: 0)
        result = 31 * result + skippedNum
        result = 31 * result + lostWP
        return result
    }
}