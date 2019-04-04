package com.alex.willtrip.core.result.implementations

import com.alex.willtrip.core.result.CurrentChain
import com.alex.willtrip.core.result.CurrentChain_
import com.alex.willtrip.core.result.MaxChain
import com.alex.willtrip.core.result.MaxChain_
import com.alex.willtrip.core.result.interfaces.ChainLoader
import com.alex.willtrip.objectbox.ObjectBox
import io.objectbox.Box

class ChainLoaderImp: ChainLoader {

    private fun getCurrentChainBox (): Box<CurrentChain> {
        return ObjectBox.boxStore.boxFor(CurrentChain::class.java)
    }

    private fun getMaxChainBox (): Box<MaxChain> {
        return ObjectBox.boxStore.boxFor(MaxChain::class.java)
    }

    override fun getCurrentChain(doId: Long): CurrentChain? {
        return getCurrentChainBox().query().equal(CurrentChain_.doId, doId).build().findUnique()
    }

    override fun getMaxChain(doId: Long): MaxChain? {
        return getMaxChainBox().query().equal(MaxChain_.doId, doId).build().findUnique()
    }

    override fun putCurrentChain(currentChain: CurrentChain) {
        val existingChain = getCurrentChain(currentChain.doId)
        if (existingChain != null) currentChain.id = existingChain.id
        getCurrentChainBox().put(currentChain)
    }

    override fun putMaxChain(maxChain: MaxChain) {
        val existingChain = getMaxChain(maxChain.doId)
        if (existingChain != null) maxChain.id = existingChain.id
        getMaxChainBox().put(maxChain)
    }
}