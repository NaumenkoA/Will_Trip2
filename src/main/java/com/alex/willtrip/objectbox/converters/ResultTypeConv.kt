package com.alex.willtrip.objectbox.converters

import com.alex.willtrip.core.result.Result
import io.objectbox.converter.PropertyConverter

class ResultTypeConv: PropertyConverter <Result.ResultType, Int> {
    override fun convertToDatabaseValue(entityProperty: Result.ResultType?): Int {
        return entityProperty?.uid ?: 0
    }

    override fun convertToEntityProperty(databaseValue: Int?): Result.ResultType {
        return when (databaseValue) {
            1 -> Result.ResultType.DONE
            2 -> Result.ResultType.SPECIAL_DAY
            3 -> Result.ResultType.SKIPPED
            else -> Result.ResultType.DEFAULT
        }
    }
}