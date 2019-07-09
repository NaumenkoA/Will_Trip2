package com.alex.willtrip.objectbox.converters

import android.net.Uri
import io.objectbox.converter.PropertyConverter

class UriConverter: PropertyConverter<Uri?, String> {

    override fun convertToDatabaseValue(entityProperty: Uri?): String {
        return entityProperty?.toString() ?: ""
    }

    override fun convertToEntityProperty(databaseValue: String?): Uri? {
        return if (databaseValue!= null) Uri.parse(databaseValue)
        else null
    }
}