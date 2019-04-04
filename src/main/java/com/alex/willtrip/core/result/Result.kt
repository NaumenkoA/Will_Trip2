package com.alex.willtrip.core.result

import com.alex.willtrip.extensions.toInt
import com.alex.willtrip.objectbox.converters.LocalDateConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import org.threeten.bp.LocalDate

@Entity
class Result (@Id var id: Long = 0,
              @Index val doId: Long,
              @Convert(converter = LocalDateConverter::class, dbType = Long::class)
              val date: LocalDate,
              val resultType: String = ResultType.DONE.name,
              val isPositive: Boolean,
              val wpPoint: Int = 0,
              val chainPoint: Int = 0,
              val note: String = "") {

    fun getTotalResult(): Int = isPositive.toInt()*(wpPoint + chainPoint)
    fun getWPPoint (): Int = isPositive.toInt()*wpPoint
    fun getChainedPoint (): Int = isPositive.toInt()*chainPoint

    fun isSpecialDay (): Boolean = resultType == ResultType.SPECIAL_DAY.name

    fun isSkipped(): Boolean = resultType == ResultType.SKIPPED.name

    fun isDone(): Boolean = resultType == ResultType.DONE.name

    override fun equals(other: Any?): Boolean {
        if (other !is Result) return false

        return (doId == other.doId && date == other.date&&
                resultType == other.resultType && isPositive == other.isPositive &&
                wpPoint == other.wpPoint && chainPoint == other.chainPoint &&
                note == other.note)
     }

    enum class ResultType {
        SPECIAL_DAY,
        SKIPPED,
        DONE
    }
}



