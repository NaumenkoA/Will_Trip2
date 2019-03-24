package com.alex.willtrip.core.do_manager

import com.alex.willtrip.extensions.toInt
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import org.threeten.bp.LocalDate


class Result (@Id var id: Long = 0,
              @Index val doLink: Int,
              val date: LocalDate,
              val isSpecialDay: Boolean = false,
              val isPositive: Boolean,
              private val wpPoint: Int = 0,
              private val chainPoint: Int = 0,
              val note: String = "") {

    fun getTotalResult(): Int = isPositive.toInt()*(wpPoint + chainPoint)
    fun getWPPoint (): Int = isPositive.toInt()*wpPoint
    fun getChainPoint (): Int = isPositive.toInt()*chainPoint

}


