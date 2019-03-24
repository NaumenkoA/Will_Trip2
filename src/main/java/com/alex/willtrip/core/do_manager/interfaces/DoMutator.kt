package com.alex.willtrip.core.do_manager.interfaces

import com.alex.willtrip.core.do_manager.Do

interface DoMutator {
    fun addNewDo (newDo: Do)
    fun removeDo (doLink: Int)
    fun removeDo (doName: String)
    fun editDo (doName: String, editedDo: Do)
    fun editDo (doLink: Int, editedDo: Do)
}