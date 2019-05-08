package com.alex.willtrip.core.result

import com.alex.willtrip.extensions.toInt
import com.alex.willtrip.objectbox.converters.LocalDateConverter
import com.alex.willtrip.objectbox.converters.ResultTypeConv
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
              @Convert(converter = ResultTypeConv::class, dbType = Int::class)
              val resultType: ResultType = ResultType.DONE,
              val isPositive: Boolean,
              val wpPoint: Int = 0,
              val chainPoint: Int = 0,
              val note: String = "") {

    fun getTotalResult(): Int = isPositive.toInt()*(wpPoint + chainPoint)
    fun getWPPoint (): Int = isPositive.toInt()*wpPoint
    fun getChainedPoint (): Int = isPositive.toInt()*chainPoint

    fun isSpecialDay (): Boolean = resultType == ResultType.SPECIAL_DAY

    fun isSkipped(): Boolean = resultType == ResultType.SKIPPED

    fun isDone(): Boolean = resultType == ResultType.DONE

    override fun equals(other: Any?): Boolean {
        if (other !is Result) return false

        return (doId == other.doId && date == other.date&&
                resultType == other.resultType && isPositive == other.isPositive &&
                wpPoint == other.wpPoint && chainPoint == other.chainPoint &&
                note == other.note)
     }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + doId.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + resultType.hashCode()
        result = 31 * result + isPositive.hashCode()
        result = 31 * result + wpPoint
        result = 31 * result + chainPoint
        result = 31 * result + note.hashCode()
        return result
    }

    enum class ResultType (val uid: Int) {
        DEFAULT (0),
        DONE (1),
        SPECIAL_DAY (2),
        SKIPPED (3)
    }
}



