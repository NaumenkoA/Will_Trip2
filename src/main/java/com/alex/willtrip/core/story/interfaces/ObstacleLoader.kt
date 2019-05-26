package com.alex.willtrip.core.story.interfaces

import com.alex.willtrip.core.story.objects.Obstacle
import org.threeten.bp.LocalDate

interface ObstacleLoader {
    fun loadObstacle (link: Int, currentDate: LocalDate): Obstacle
    fun saveObstacle (obstacle: Obstacle)
}