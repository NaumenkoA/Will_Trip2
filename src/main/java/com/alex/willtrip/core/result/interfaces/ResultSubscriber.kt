package com.alex.willtrip.core.result.interfaces

import com.alex.willtrip.core.result.Result
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate


interface ResultSubscriber {
    fun addObserver (date: LocalDate, observer: DataObserver<List<Result>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}