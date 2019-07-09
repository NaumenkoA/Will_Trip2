package com.alex.willtrip.core.gratitude

import android.net.Uri
import com.alex.willtrip.objectbox.converters.LocalDateConverter
import com.alex.willtrip.objectbox.converters.UriConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import org.threeten.bp.LocalDate

@Entity
data class Gratitude (@Id var id: Long = 0,
                 @Convert(converter = LocalDateConverter::class, dbType = Long::class)
                 val date: LocalDate, val text: String,
                 @Convert(converter = UriConverter::class, dbType = String::class)
                 val photoUri: Uri?)