package com.alex.willtrip.willpower

import com.alex.willtrip.willpower.interfaces.WPMutator

class Mutator (var wp: WillPower): WPMutator {

    constructor() : this(WillPower())

    override fun setWP(wp: WillPower) {
        this.wp = wp
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