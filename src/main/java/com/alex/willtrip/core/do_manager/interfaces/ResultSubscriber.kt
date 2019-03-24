package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Result
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import java.time.LocalDate

interface ResultSubscriber {
    fun addObserver (date: LocalDate, observer: DataObserver<List<Result>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}