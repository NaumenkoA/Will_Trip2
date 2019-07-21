package com.alex.willtrip.objectbox.helpers

import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.converters.LocalDateConverter
import io.objectbox.Box
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import org.threeten.bp.LocalDate

@Entity
class DateEntity (@Id var id: Long = 0, @Index val link: Long,
                  @Convert(converter = LocalDateConverter::class, dbType = Long::class)
                  val date: LocalDate)

class DateSaver {

  private fun getBox (): Box<DateEntity> {
    return ObjectBox.boxStore.boxFor(DateEntity::class.java)
  }

  fun getDate (link: Long): LocalDate? {
    return getBox().query().equal(DateEntity_.link, link).build().findUnique()?.date
  }

  fun saveDate (link: Long, date: LocalDate) {
    val id = getBox().query().equal(DateEntity_.link, link).build().findUnique()?.id ?: 0
    getBox().put(DateEntity(id, link, date))
  }
}