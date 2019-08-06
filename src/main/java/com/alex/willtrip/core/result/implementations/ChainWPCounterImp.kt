package com.alex.willtrip.core.result.implementations

import com.alex.willtrip.core.result.CurrentChain
import com.alex.willtrip.core.result.interfaces.ChainWPCounter

class ChainWPCounterImp: ChainWPCounter {

  override fun countChainWP(doComplexity: Int, currentChain: CurrentChain): Int {
    var plusValue = 0

    when (currentChain.length) {
      in 3..4 -> plusValue = 1
      in 5..6 -> plusValue = 2
      in 7..9 -> plusValue = 3
      in 10..13 -> plusValue = 4
      in 14..Int.MAX_VALUE -> plusValue = 5
    }

    return Math.min(doComplexity, plusValue)
  }

}