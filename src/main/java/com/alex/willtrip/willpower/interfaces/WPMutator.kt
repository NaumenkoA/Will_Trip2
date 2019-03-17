package com.alex.willtrip.willpower.interfaces

import com.alex.willtrip.willpower.WillPower

interface WPMutator {
    fun setWP(willPower: WillPower)
    fun increase (value: Int): Int
    fun decrease (value: Int): Int
    fun getCurrentWP (): Int
}