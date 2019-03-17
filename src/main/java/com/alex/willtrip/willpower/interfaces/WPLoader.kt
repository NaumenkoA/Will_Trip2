package com.alex.willtrip.willpower.interfaces

import com.alex.willtrip.willpower.WillPower

interface WPLoader {
    fun loadWillPower (): WillPower?
    fun saveWillPower(willPower: WillPower)
}