package com.alex.willtrip.objectbox.converters

import io.objectbox.converter.PropertyConverter

class ListOfIntConverter: PropertyConverter <List<Int>, String> {

    override fun convertToDatabaseValue(entityProperty: List<Int>?): String {
        if (entityProperty == null || entityProperty.isEmpty()) return ""
        var prefix = ""
        val sb = StringBuilder()
        entityProperty.forEach {
            sb.append(prefix)
            prefix = ","
            sb.append(it)
        }
        return sb.toString()
    }

    override fun convertToEntityProperty(databaseValue: String?): List<Int> {
        if (databaseValue == null || databaseValue == "") return emptyList()
        val listOfString = databaseValue.split(",")
        val listOfInt = mutableListOf<Int>()
        listOfString.forEach {
            listOfInt.add(it.toInt())
        }
        return listOfInt
    }
}