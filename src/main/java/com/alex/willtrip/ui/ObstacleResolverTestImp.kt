package com.alex.willtrip.ui

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.di.DaggerAppComponent
import com.alex.willtrip.di.DaggerEveryDayBehaviorComponent
import org.threeten.bp.LocalDate

class ObstacleResolverTestImp {

    private var storyManager = DaggerAppComponent.create().storyManager()
    private val wpManager = DaggerAppComponent.create().wpManager()
    private val resultManager = DaggerAppComponent.create().resultManager()
    private val doManager = DaggerAppComponent.create().doManager()
    private val currentDate = LocalDate.of(2019, 5,26)


    fun resolveObstacles() {
        if (!storyManager.checkObstaclesResolved(currentDate)) {

            val obstacles = storyManager.getCurrentScene(currentDate).obstacles
            obstacles.forEach {

                when (it) {
                    is ObstacleWP -> {
                        val diff = it.totalValue - wpManager.getCurrentWP()
                        wpManager.increaseWP(diff)
                    }
                    is ObstacleBonus -> {
                        wpManager.increaseWP(it.totalValue)
                        it.isBonusGranted = true
                        storyManager.bonusGranted(it)
                    }
                    is ObstacleComp -> {
                        val doObj1 = Do(
                            name = "Test + ${wpManager.getCurrentWP()}",
                            periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
                            note = "Test do",
                            isSpecialDayEnabled = true,
                            isPositive = false,
                            complexity = it.totalValue,
                            startDate = LocalDate.of(2019, 4, 3),
                            expireDate = LocalDate.of(2019, 12, 31)
                        )
                        doManager.addNewDo(doObj1)
                    }
                    is ObstacleCount -> {
                        val doCount = doManager.getActualDoForDate(currentDate)?.size ?: 0
                        val diff = it.totalValue - doCount
                        var i = 0
                        while (diff - i > 0) {
                            val doObj1 = Do(
                                name = "Test+1+$i",
                                periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
                                note = "Test do",
                                isSpecialDayEnabled = true,
                                isPositive = false,
                                complexity = 3,
                                startDate = LocalDate.of(2019, 4, 3),
                                expireDate = LocalDate.of(2019, 12, 31)
                            )
                            doManager.addNewDo(doObj1)
                            i++
                        }
                    }
                    is ObstacleChain -> {
                        val doList = doManager.getActualDoForDate(currentDate)
                        if (doList.isNullOrEmpty()) {
                            val doObj1 = Do(
                                name = "Test+X",
                                periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
                                note = "Test do",
                                isSpecialDayEnabled = true,
                                isPositive = false,
                                complexity = 2,
                                startDate = LocalDate.of(2019, 4, 3),
                                expireDate = LocalDate.of(2019, 12, 31)
                            )
                            doManager.addNewDo(doObj1)
                        }
                        val doList1 = doManager.getActualDoForDate(currentDate)
                        var maxChain = Pair(0, 0)
                        doList1?.forEach {
                            val chain = resultManager.getMaxChain(it.id)?.length ?: 0
                            if (chain >= maxChain.second) maxChain = Pair(it.id.toInt(), chain)
                        }
                        val diff =  it.totalValue - maxChain.second
                        var i = 0
                        val results = resultManager.getNLastResults(maxChain.first.toLong(), 1000)
                        val date = if (results.isNotEmpty()) results.last().date.minusDays(1) else currentDate
                        while (diff - i >= 0) {
                            val result1 = Result(
                                doId = maxChain.first.toLong(),
                                date = date.minusDays(i.toLong()),
                                isPositive = true,
                                wpPoint = 3,
                                chainPoint = 1
                            )
                            resultManager.addResult(result1)
                            i++
                        }
                    }
                }
            }
        }
    }
}