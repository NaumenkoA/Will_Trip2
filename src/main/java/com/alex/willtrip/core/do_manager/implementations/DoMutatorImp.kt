package com.alex.willtrip.core.do_manager.implementations

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.interfaces.DoMutator
import com.alex.willtrip.extensions.toDoDB
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.DoDB
import com.alex.willtrip.objectbox.class_boxes.DoDB_
import io.objectbox.Box
import java.lang.IllegalArgumentException

class DoMutatorImp: DoMutator {

    private fun getDoDBBox (): Box<DoDB> {
        return ObjectBox.boxStore.boxFor(DoDB::class.java)
    }

    override fun removeDo(doId: Long) {
        getDoDBBox().query().equal(DoDB_.id, doId).build().findUnique() ?: throw IllegalArgumentException ("${DoMutatorImp::class.java.simpleName}: " +
                "no Do object with ID: \"$doId\" was found. Delete operation wasn't successful")
        getDoDBBox().remove(doId)
    }

    override fun removeDo(doName: String) {
        val doDB = getDoDBBox().query().equal(DoDB_.name, doName).build().findUnique() ?: throw IllegalArgumentException ("${DoMutatorImp::class.java.simpleName}: " +
                "no Do object with the name \"$doName\" was found. Delete operation wasn't successful")
        getDoDBBox().remove(doDB.id)
    }

    override fun editDo(doName: String, editedDo: Do) {
        val doDB = getDoDBBox().query().equal(DoDB_.name, doName).build().findUnique() ?: throw IllegalArgumentException ("${DoMutatorImp::class.java.simpleName}: " +
                "no Do object with the name \"$doName\" was found. Edit operation wasn't successful")
        editedDo.id = doDB.id
        getDoDBBox().put(editedDo.toDoDB())
    }

    override fun editDo(doId: Long, editedDo: Do) {
        editedDo.id = doId
        getDoDBBox().put(editedDo.toDoDB())
    }

    override fun addNewDo(newDo: Do): Long {
        return getDoDBBox().put(newDo.toDoDB())
    }
}