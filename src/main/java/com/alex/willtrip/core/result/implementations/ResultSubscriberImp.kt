package com.alex.willtrip.core.result.implementations

import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.Result_
import com.alex.willtrip.core.result.interfaces.ResultSubscriber
import com.alex.willtrip.objectbox.ObjectBox
import io.objectbox.Box
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate

class ResultSubscriberImp: ResultSubscriber {

    private fun getResultBox(): Box<Result> {
        return ObjectBox.boxStore.boxFor(Result::class.java)
    }

    override fun addObserver(date: LocalDate, observer: DataObserver<List<Result>>): DataSubscription {
        val query = getResultBox().query().equal(Result_.date, date.toEpochDay()).build()
        return query.subscribe().observer(observer)
    }

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }
}