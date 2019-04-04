package com.alex.willtrip.core.result

import com.alex.willtrip.objectbox.converters.LocalDateConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import org.threeten.bp.LocalDate

@Entity
class CurrentChain (@Id var id: Long = 0, val doId: Long, val length: Int,
                    @Convert(converter = LocalDateConverter::class, dbType = Long::class)
                val startDate: LocalDate)