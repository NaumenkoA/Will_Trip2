package com.alex.willtrip.core.willpower.interfaces

import com.alex.willtrip.core.willpower.WillPower

interface WPLoader {
  fun loadWillPower (): WillPower?
  fun saveWillPower(willPower: WillPower)
}