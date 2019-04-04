package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Do

interface DoMutator {
    fun addNewDo (newDo: Do): Long
    fun removeDo (doId: Long)
    fun removeDo (doName: String)
    fun editDo (doName: String, editedDo: Do)
    fun editDo (doId: Long, editedDo: Do)
}