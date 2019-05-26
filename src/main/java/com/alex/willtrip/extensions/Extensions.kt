package com.alex.willtrip.extensions

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.period.*
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.di.*
import com.alex.willtrip.objectbox.class_boxes.DoDB
import com.alex.willtrip.objectbox.class_boxes.ObstacleDB
import com.alex.willtrip.objectbox.class_boxes.ObstacleType
import com.alex.willtrip.objectbox.class_boxes.ObstacleType.*
import com.alex.willtrip.objectbox.class_boxes.PeriodBehaviourType
import java.lang.Exception
import java.lang.IllegalArgumentException

fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

fun Do.toDoDB (): DoDB {
    val doDB = DoDB (id, name, note, isPositive, complexity, isSpecialDayEnabled,
        startDate, expireDate)

    when (periodBehavior) {
        is DaysOfWeekBehavior -> {
            doDB.periodBehaviorType = PeriodBehaviourType.DAYS_OF_WEEK.name
            doDB.list = periodBehavior.daysList
        }

        is EveryDayBehavior -> {
            doDB.periodBehaviorType = PeriodBehaviourType.EVERY_DAY.name
            doDB.list = emptyList()
        }

        is EveryNDaysBehavior -> {
            doDB.periodBehaviorType = PeriodBehaviourType.EVERY_N_DAY.name
            doDB.list = listOf(periodBehavior.repeatPeriod)
        }

        is NTimesAWeekBehavior -> {
            doDB.periodBehaviorType = PeriodBehaviourType.N_TIMES_A_WEEK.name
            doDB.list = listOf(periodBehavior.timesAWeek)
        }
    }

    return doDB
}

fun List<DoDB>?.toDoList(): List<Do>? {
    val listOfDo = mutableListOf<Do>()
    if (this == null) return null
    this.forEach {
        listOfDo.add(it.toDo())
    }
    return listOfDo
}

fun DoDB.toDo(): Do {
    lateinit var periodBehavior:PeriodBehavior

    when (periodBehaviorType) {
        PeriodBehaviourType.DAYS_OF_WEEK.name -> {
            periodBehavior = DaggerDaysOfWeekBehaviorComponent.builder().
                daysOfWeekBehaviorModule(DaysOfWeekBehaviorModule(list)).build().daysOfWeekBehavior()
        }

        PeriodBehaviourType.N_TIMES_A_WEEK.name -> {
            periodBehavior = DaggerNTimesAWeekBehaviorComponent.builder().
                nTimesAWeekBehaviorModule(NTimesAWeekBehaviorModule(list[0])).build().nTimesAWeekBehavior()
        }

        PeriodBehaviourType.EVERY_N_DAY.name -> {
            periodBehavior = DaggerEveryNDaysBehaviorComponent.builder().appComponent(DaggerAppComponent.create()).
                everyNDaysBehaviorModule(EveryNDaysBehaviorModule(list[0])).build().everyNDaysBehavior()
        }

        PeriodBehaviourType.EVERY_DAY.name -> {
            periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior()
        }

        else -> throw Exception ("Error in convert func DoDb.toDo(), unexpected PeriodBehaviourType:" +
                periodBehaviorType
        )
    }

    return Do (id, name, periodBehavior, note, isPositive, complexity, isSpecialDayEnabled, startDate, expireDate)
}

fun Story.getRandomLink (firstLink: Int, secondLink: Int):Int {
    val random = java.util.Random()
    val int = random.nextInt(2)
    when (int) {
        0 -> return firstLink
        1 -> return secondLink
        else -> return firstLink
    }
}

fun Obstacle.toObstacleDB (): ObstacleDB {
    when (this) {
        is ObstacleWP -> return ObstacleDB(link = this.link, textId = this.textId, addValue = this.addValue,
            minValue = this.minValue, totalValue = this.totalValue, type = WP
        )

        is ObstacleComp -> return ObstacleDB(link = this.link, textId = this.textId, totalValue = this.totalValue,
            type = COMP
        )

        is ObstacleChain -> return ObstacleDB(link = this.link, textId = this.textId, addValue = this.addValue,
            minValue = this.minValue, totalValue = this.totalValue, type = CHAIN
        )

        is ObstacleCount -> return ObstacleDB(link = this.link, textId = this.textId, addValue = this.addValue,
            minValue = this.minValue, totalValue = this.totalValue, type = COUNT
        )

        is ObstacleBonus -> return ObstacleDB(link = this.link, textId = this.textId, isBonusGranted = this.isBonusGranted,
            totalValue = this.totalValue, type = BONUS
        )

        else -> throw IllegalArgumentException ("No such type of Obstacle: ${this::class.java.simpleName} was found")
    }
}

fun ObstacleDB.toObstacle(): Obstacle {
    return when (this.type) {
        WP -> ObstacleWP(link, textId, addValue, minValue, totalValue)
        COMP -> ObstacleComp(link, textId, totalValue)
        CHAIN -> ObstacleChain(link, textId, addValue, minValue, totalValue)
        COUNT -> ObstacleCount(link, textId, addValue, minValue, totalValue)
        BONUS -> ObstacleBonus(link, textId, isBonusGranted, totalValue)
        else -> throw IllegalArgumentException("Illegal ObstacleDB type: ${this.type} at convert operation to Obstacle")
    }
}

fun List<Obstacle>.convertToObstacleDBList(): List <ObstacleDB> {
    val list = mutableListOf<ObstacleDB>()

    this.forEach {
        list.add (it.toObstacleDB())
    }
    return list
}