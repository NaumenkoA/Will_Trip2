package com.alex.willtrip.core.story.implementations

import com.alex.willtrip.core.story.interfaces.ObstacleResolver
import com.alex.willtrip.core.story.objects.Obstacle
import org.threeten.bp.LocalDate

class ObstacleResolverImp: ObstacleResolver {

    override fun checkAllObstaclesResolved(currentDate: LocalDate, obstacleList: List<Obstacle>): Boolean {
        obstacleList.forEach {
            if (!it.isResolved(currentDate)) return false
        }
        return true
    }

    override fun mapObstaclesResolved(currentDate: LocalDate, obstacleList: List<Obstacle>): List<Pair<Obstacle, Boolean>> {
        val list = mutableListOf<Pair<Obstacle, Boolean>>()
        obstacleList.forEach {
            list.add(Pair(it, it.isResolved(currentDate)))
        }
        return list
    }
}