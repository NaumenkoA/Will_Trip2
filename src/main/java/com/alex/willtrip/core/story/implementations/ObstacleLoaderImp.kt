package com.alex.willtrip.core.story.implementations

import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.story.interfaces.ObstacleLoader
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.core.willpower.WPManager
import com.alex.willtrip.di.DaggerAppComponent
import com.alex.willtrip.extensions.toObstacle
import com.alex.willtrip.extensions.toObstacleDB
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.ObstacleDB
import com.alex.willtrip.objectbox.class_boxes.ObstacleDB_
import io.objectbox.Box
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException
import kotlin.math.max

class ObstacleLoaderImp (val wpManager: WPManager, val doManager: DoManager, val resultManager: ResultManager): ObstacleLoader {

    private fun getObstacleDBBox(): Box<ObstacleDB> {
        return ObjectBox.boxStore.boxFor(ObstacleDB::class.java)
    }

    override fun saveObstacle(obstacle: Obstacle) {
        val obstacleId = getObstacleDBBox().query().equal(ObstacleDB_.link, obstacle.link.toLong()).
                build().findUnique()?.id ?: 0
        val obstacleDB = obstacle.toObstacleDB()
        obstacleDB.id = obstacleId
        getObstacleDBBox().put(obstacleDB)
    }

    override fun loadObstacle(link: Int, currentDate: LocalDate): Obstacle {
       val obstacleDB = getObstacleDBBox().query().equal(ObstacleDB_.link, link.toLong()).
               build().findUnique() ?: throw IllegalArgumentException ("${ObstacleLoaderImp::class.java.simpleName}: not found ObstacleDB with link: $link")
            val obstacle = obstacleDB.toObstacle()

        val initializedObstacle = getInitializedObstacle (obstacle)

        val obstacleWithTotalValue = getObstacleWithTotalValue(initializedObstacle, currentDate)

        saveObstacle(obstacleWithTotalValue)

       return getInitializedObstacle(obstacleWithTotalValue)
    }

    private fun getObstacleWithTotalValue(obstacle: Obstacle, currentDate: LocalDate): Obstacle {
        when (obstacle) {
            is ObstacleWP -> {
                return if (obstacle.totalValue == 0) {
                    val totalValue = max(obstacle.minValue, wpManager.getCurrentWP() + obstacle.addValue)
                    val trimmedTotalValue = trim (totalValue)
                    ObstacleWP(obstacle.link, obstacle.textId, obstacle.addValue, obstacle.minValue, trimmedTotalValue)
                } else obstacle
            }

            is ObstacleChain -> {
                return if (obstacle.totalValue == 0) {
                    val doList = doManager.getActualDoForDate(currentDate) ?: emptyList()
                    val totalValue = Math.max(obstacle.minValue, obstacle.getMaxChainLength(doList) + obstacle.addValue)
                    ObstacleChain(obstacle.link, obstacle.textId, obstacle.addValue, obstacle.minValue, totalValue)
                } else obstacle
            }
            is ObstacleCount -> {
                return if (obstacle.totalValue == 0) {
                    val doCount = (doManager.getActualDoForDate(currentDate)?.size ?: 0) + 1
                    ObstacleCount(obstacle.link, obstacle.textId, obstacle.addValue, obstacle.minValue, doCount)
                } else obstacle
            }
            else -> return obstacle
        }
    }

    private fun trim(totalValue: Int): Int {
        return when (totalValue) {
            in (0..10) ->  totalValue
            in (11..100) -> {
                val trimmed = if (totalValue%5 != 0) (totalValue/5)*5 + 5
                else totalValue
                trimmed
            }
            in (101..Int.MAX_VALUE) -> {
                val trimmed = if (totalValue%10 != 0) (totalValue/10)*10 + 10
                else totalValue
                trimmed
            }
            else -> totalValue
        }
    }

    private fun getInitializedObstacle(obstacle: Obstacle): Obstacle {
        when (obstacle) {
            is ObstacleWP -> {
                obstacle.wpManager = wpManager
                return obstacle
            }
            is ObstacleComp -> {
                obstacle.doManager = doManager
                return obstacle
            }
            is ObstacleChain -> {
                obstacle.doManager = doManager
                obstacle.resultManager = resultManager
                return obstacle
            }
            is ObstacleCount -> {
                obstacle.doManager = doManager
                return obstacle
            }
            else -> return obstacle
        }
    }
}