package com.alex.willtrip.objectbox.class_boxes

import com.alex.willtrip.objectbox.converters.ListOfIntConverter
import com.alex.willtrip.objectbox.converters.LocalDateConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import org.threeten.bp.LocalDate

@Entity
class DoDB (@Id var id: Long = 0,
            val name: String = "",
            val note: String? = null,
            val isPositive: Boolean = false,
            val complexity: Int = 0,
            val isSpecialDayEnabled: Boolean = false,
            @Convert(converter = LocalDateConverter::class, dbType = Long::class)
            val startDate: LocalDate = LocalDate.MIN,
            @Convert(converter = LocalDateConverter::class, dbType = Long::class)
            val expireDate: LocalDate = LocalDate.MAX) {

    lateinit var periodBehaviorType: String

    @Convert(converter = ListOfIntConverter::class, dbType = String::class)
     var list: List<Int> = emptyList()
}

enum class PeriodBehaviourType {
    DAYS_OF_WEEK,
    EVERY_DAY,
    EVERY_N_DAY,
    N_TIMES_A_WEEK
}
