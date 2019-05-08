package com.alex.willtrip.extensions

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.period.*
import com.alex.willtrip.di.*
import com.alex.willtrip.objectbox.class_boxes.DoDB
import com.alex.willtrip.objectbox.class_boxes.PeriodBehaviourType
import java.lang.Exception

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

