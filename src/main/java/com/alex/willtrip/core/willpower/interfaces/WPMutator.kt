package com.alex.willtrip.core.willpower.interfaces

import com.alex.willtrip.core.willpower.WillPower

interface WPMutator {
  fun setWP(willPower: WillPower)
  fun increase (value: Int): Int
  fun decrease (value: Int): Int
  fun getCurrentWP (): Int
}
