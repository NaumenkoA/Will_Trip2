package com.alex.willtrip.core.story.objects

import org.threeten.bp.LocalDate

abstract class Obstacle (val link: Int, val textId: Int, val totalValue: Int) {

    abstract fun isResolved(currentDate: LocalDate): Boolean

}