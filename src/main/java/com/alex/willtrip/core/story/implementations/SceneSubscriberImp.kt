package com.alex.willtrip.core.story.implementations

import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.core.story.interfaces.SceneSubscriber
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.SettingEntity_
import com.alex.willtrip.objectbox.helpers.IntSaver
import com.alex.willtrip.objectbox.helpers.IntSaver_
import io.objectbox.Box
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class SceneSubscriberImp(): SceneSubscriber {

    private fun getIntBox (): Box<IntSaver> {
        return ObjectBox.boxStore.boxFor(IntSaver::class.java)
    }

    override fun addObserver(observer: DataObserver<Int>): DataSubscription {
        val query = getIntBox().query().equal(IntSaver_.link, 1).build()
        return query.subscribe().
            transform {
                query.findUnique()?.value ?: 1
                }.observer(observer)
    }

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }
}