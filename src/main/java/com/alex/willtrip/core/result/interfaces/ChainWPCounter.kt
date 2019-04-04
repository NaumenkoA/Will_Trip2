package com.alex.willtrip.core.result.interfaces

import com.alex.willtrip.core.result.CurrentChain

interface ChainWPCounter {
    fun countChainWP(doComplexity: Int, currentChain: CurrentChain)
}