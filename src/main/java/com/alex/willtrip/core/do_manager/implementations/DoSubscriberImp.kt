package com.alex.willtrip.core.do_manager.implementations

import com.alex.willtrip.core.do_manager.interfaces.DoSubscriber
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.DoDB
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class DoSubscriberImp: DoSubscriber {

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }

    override fun addObserver(observer: DataObserver<Class<DoDB>>): DataSubscription {
        return ObjectBox.boxStore.subscribe(DoDB::class.java).observer(observer)
    }
}