package com.alex.willtrip.objectbox.class_boxes

import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.core.willpower.WillPower
import com.alex.willtrip.core.willpower.WillPower_
import com.alex.willtrip.core.willpower.interfaces.WPSubscriber
import com.alex.willtrip.core.willpower.interfaces.WPLoader
import io.objectbox.Box
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class WPObjectbox : WPLoader, WPSubscriber {

    private val query by lazy {
        getBox().query().equal(WillPower_.id, 0).build()
    }


    private fun getBox (): Box <WillPower> {
        return ObjectBox.boxStore.boxFor(WillPower::class.java)
    }

    override fun loadWillPower(): WillPower? {
        if (getBox().isEmpty) return null
        return getBox().all[0]
    }

    private fun loadWillPowerAsInt(): Int {
        return if (getBox().all.isEmpty()) {
            0
        } else getBox().all[0].willPower
    }

    override fun saveWillPower(willPower: WillPower) {
        if (getBox().all.isNotEmpty()) willPower.id = getBox().all[0].id
        getBox().put(willPower)
    }

    override fun addObserver(observer: DataObserver<Int>): DataSubscription {
        return query.subscribe().transform { loadWillPowerAsInt()}.observer(observer)
    }

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }
}

