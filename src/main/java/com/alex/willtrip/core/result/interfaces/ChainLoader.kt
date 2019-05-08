package com.alex.willtrip.core.result.interfaces

import com.alex.willtrip.core.result.CurrentChain
import com.alex.willtrip.core.result.MaxChain

interface ChainLoader {
    fun getCurrentChain (doId: Long): CurrentChain?
    fun getMaxChain (doId: Long): MaxChain?
    fun putCurrentChain (currentChain: CurrentChain)
    fun cancelCurrentChain (doId: Long)
    fun putMaxChain (maxChain: MaxChain)
}