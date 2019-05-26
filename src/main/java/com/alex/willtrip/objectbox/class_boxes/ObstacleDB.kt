package com.alex.willtrip.objectbox.class_boxes

import com.alex.willtrip.objectbox.converters.ObstacleTypeConv
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
class ObstacleDB (@Id var id: Long = 0, @Index val link: Int = 0,
                  @Convert(converter = ObstacleTypeConv::class, dbType = Int::class)
                  val type: ObstacleType,
                  val textId: Int = 0, val addValue: Int = 0, val minValue: Int = 0,
                  val totalValue: Int = 0, val isBonusGranted: Boolean = false)

enum class ObstacleType (val uid: Int) {
    WP (1),
    CHAIN (2),
    COMP (3),
    COUNT (4),
    BONUS (5),
    DEFAULT (0)
}