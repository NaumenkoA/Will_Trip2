package com.alex.willtrip.objectbox.converters

import io.objectbox.converter.PropertyConverter
import org.threeten.bp.LocalDate

class LocalDateConverter: PropertyConverter <LocalDate, Long> {

    override fun convertToDatabaseValue(entityProperty: LocalDate?): Long = entityProperty?.toEpochDay() ?: 0

    override fun convertToEntityProperty(databaseValue: Long?): LocalDate {
        if (databaseValue == null || databaseValue.equals(0)) return LocalDate.MIN
        return LocalDate.ofEpochDay(databaseValue)
    }
}