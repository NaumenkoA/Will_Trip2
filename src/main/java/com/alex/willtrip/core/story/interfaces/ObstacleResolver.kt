package com.alex.willtrip.core.story.interfaces

import com.alex.willtrip.core.story.objects.Obstacle
import org.threeten.bp.LocalDate

interface ObstacleResolver {
    fun checkAllObstaclesResolved(currentDate: LocalDate, obstacleList: List<Obstacle>): Boolean
    fun mapObstaclesResolved (currentDate: LocalDate, obstacleList: List<Obstacle>): List<Pair<Obstacle, Boolean>>
}