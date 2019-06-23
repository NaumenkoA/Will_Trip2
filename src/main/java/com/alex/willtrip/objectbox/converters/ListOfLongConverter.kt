package com.alex.willtrip.objectbox.converters

import io.objectbox.converter.PropertyConverter

class ListOfLongConverter: PropertyConverter <List<Long>, String> {

    override fun convertToDatabaseValue(entityProperty: List<Long>?): String {
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

    override fun convertToEntityProperty(databaseValue: String?): List<Long> {
        if (databaseValue == null || databaseValue == "") return emptyList()
        val listOfString = databaseValue.split(",")
        val listOfLong = mutableListOf<Long>()
        listOfString.forEach {
            listOfLong.add(it.toLong())
        }
        return listOfLong
    }
}