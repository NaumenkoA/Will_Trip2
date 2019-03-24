package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Do
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import java.time.LocalDate

interface DoSubscriber {
    fun addObserver (date: LocalDate, observer: DataObserver<List<Do>>): DataSubscription
    fun removeObserver (dataSubscription: DataSubscription)
}