package com.alex.willtrip.core.gratitude

import com.alex.willtrip.objectbox.converters.LocalDateConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import org.threeten.bp.LocalDate

@Entity
class GratitudeWPBonus (@Id var id: Long = 0,
                        @Convert(converter = LocalDateConverter::class, dbType = Long::class)
                        val date: LocalDate)