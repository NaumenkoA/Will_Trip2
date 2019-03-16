package com.alex.willtrip.willpower.interfaces

interface WPMutator {
    fun increase (value: Int): Int
    fun decrease (value: Int): Int
    fun getCurrentWP (): Int
}