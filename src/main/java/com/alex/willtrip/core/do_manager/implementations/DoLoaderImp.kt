package com.alex.willtrip.core.do_manager.implementations

import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.interfaces.DoLoader
import com.alex.willtrip.extensions.toDo
import com.alex.willtrip.extensions.toDoList
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.DoDB
import com.alex.willtrip.objectbox.class_boxes.DoDB_
import io.objectbox.Box
import org.threeten.bp.LocalDate


class DoLoaderImp: DoLoader {

    private fun getDoDBBox (): Box<DoDB> {

        return ObjectBox.boxStore.boxFor(DoDB::class.java)
    }

    override fun loadAllDo(): List<Do>? {
        val doDBList = getDoDBBox().all
        return doDBList.toDoList()
    }

    override fun checkDoNameExists(doName: String): Boolean {
        val doDB = getDoDBBox().query().equal(DoDB_.name, doName).build().findUnique()
        return (doDB != null)
    }

    override fun getIdByName(doName: String): Long? {
        val doDB = getDoDBBox().query().equal(DoDB_.name, doName).build().findUnique()
        return doDB?.id
    }

    override fun loadDoById(id: Long): Do? {
        return getDoDBBox().get(id)?.toDo()
    }

    override fun loadDoByName(name: String): Do? {
        return getDoDBBox().query().equal(DoDB_.name, name).build().findUnique()?.toDo()
    }

    override fun loadAllExceptArchive(date: LocalDate): List<Do>? {
        val doDBList = getDoDBBox().query().
            greater(DoDB_.expireDate, date.minusDays(1).toEpochDay()).build().find()
        return doDBList.toDoList()
    }

    override fun loadActualDoForDate(date: LocalDate): List<Do>? {
        val doDBList = getDoDBBox().query().less(DoDB_.startDate, date.plusDays(1).toEpochDay())
            .greater(DoDB_.expireDate, date.minusDays(1).toEpochDay()).build().find()
        return doDBList.toDoList()
    }

    override fun loadArchiveDoForDate(date: LocalDate): List<Do>? {
        val doDBList = getDoDBBox().query().less(DoDB_.expireDate, date.toEpochDay()).build().find()
        return doDBList.toDoList()
    }
}

