package com.alex.willtrip.core.story.objects

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.willpower.WPManager
import com.alex.willtrip.di.DaggerAppComponent
import org.threeten.bp.LocalDate

class ObstacleWP (link: Int, textId: Int, val addValue: Int = 0, val minValue: Int = 0, totalValue: Int = 0): Obstacle(link, textId, totalValue) {

    lateinit var wpManager:WPManager

    override fun isResolved(currentDate: LocalDate): Boolean {
        return wpManager.getCurrentWP() >= totalValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ObstacleWP) return false
        return (link == other.link && textId == other.textId && addValue==other.addValue&&minValue==other.minValue&&totalValue == other.totalValue)
    }

    override fun hashCode(): Int {
        var result = addValue
        result = 31 * result + minValue
        result = 31 * result + wpManager.hashCode()
        return result
    }
}

class ObstacleComp (link: Int, textId: Int,  totalValue: Int): Obstacle(link, textId, totalValue) {

   lateinit var doManager: DoManager

    override fun isResolved(currentDate: LocalDate): Boolean {
        val doList = doManager.getActualDoForDate(currentDate)
        val maxCompDo = doList?.maxBy { it.complexity }
        val maxComp = maxCompDo?.complexity ?: 0

        return maxComp >= totalValue
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ObstacleComp) return false
        return (link == other.link && textId == other.textId && totalValue == other.totalValue)
    }

    override fun hashCode(): Int {
        return doManager.hashCode()
    }
}

class ObstacleChain (link: Int, textId: Int, val addValue: Int = 0, val minValue: Int = 0, totalValue: Int = 0): Obstacle(link, textId, totalValue) {

    lateinit var doManager:DoManager
    lateinit var resultManager: ResultManager

    override fun isResolved(currentDate: LocalDate): Boolean {
        val doList = doManager.getActualDoForDate(currentDate)
        if (doList.isNullOrEmpty()) return false
        val maxChainLength = getMaxChainLength(doList)

        return maxChainLength >= totalValue
    }

    fun getMaxChainLength(doList: List<Do>): Int {

        var maxChainLength = 0
        doList.forEach {
            val maxChain = resultManager.getMaxChain(it.id)
            if (maxChain != null && maxChain.length > maxChainLength) maxChainLength = maxChain.length
        }
        return maxChainLength
    }
}

    class ObstacleCount(link: Int, textId: Int, val addValue: Int = 0, val minValue: Int = 0, totalValue: Int = 0) :
        Obstacle(link, textId, totalValue) {

        lateinit var doManager:DoManager

        override fun isResolved(currentDate: LocalDate): Boolean {
            val doCount = doManager.getActualDoForDate(currentDate)?.size ?: 0
            return doCount >= totalValue
        }
    }

    class ObstacleBonus(link: Int, textId: Int, var isBonusGranted: Boolean = false, totalValue: Int) :
        Obstacle(link, textId, totalValue) {
        override fun isResolved(currentDate: LocalDate): Boolean = isBonusGranted
    }


