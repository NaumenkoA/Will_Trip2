package com.alex.willtrip.objectbox.converters

import com.alex.willtrip.core.story.objects.Obstacle
import com.alex.willtrip.objectbox.class_boxes.ObstacleType
import io.objectbox.converter.PropertyConverter

class ObstacleTypeConv: PropertyConverter<ObstacleType, Int> {
    override fun convertToDatabaseValue(entityProperty: ObstacleType?): Int {
        return entityProperty?.uid ?: 0
    }

    override fun convertToEntityProperty(databaseValue: Int?): ObstacleType {
        return when (databaseValue) {
            1 -> ObstacleType.WP
            2 -> ObstacleType.CHAIN
            3 -> ObstacleType.COMP
            4 -> ObstacleType.COUNT
            5 -> ObstacleType.BONUS
            0 -> ObstacleType.DEFAULT
            else -> ObstacleType.DEFAULT
        }
    }
}