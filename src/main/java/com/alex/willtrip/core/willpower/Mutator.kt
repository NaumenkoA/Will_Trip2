package com.alex.willtrip.core.willpower

import com.alex.willtrip.core.willpower.interfaces.WPMutator

class Mutator (var wp: WillPower): WPMutator {

  constructor() : this(WillPower())

  override fun setWP(willPower: WillPower) {
    this.wp = willPower
  }

  override fun getCurrentWP(): Int {
    return wp.willPower
  }

  override fun increase(value: Int): Int {
    wp.willPower += value
    return wp.willPower
  }

  override fun decrease(value: Int): Int {
    wp.willPower -= value

    return wp.willPower
  }
}