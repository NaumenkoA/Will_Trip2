package com.alex.willtrip.ui.adapter.data

class OpenAction (val doId: Long, val name: String, val isObligatory: Boolean, val isPositive: Boolean,  val complexity: Int, val chainWP: Int, val chainLength: Int, val isSpecialDayEnabled: Boolean) {

    val totalWP
    get() = complexity + chainWP
}