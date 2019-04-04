package com.alex.willtrip.core.do_manager

import com.alex.willtrip.core.do_manager.interfaces.DoLoader
import com.alex.willtrip.core.do_manager.interfaces.DoMutator
import com.alex.willtrip.core.do_manager.interfaces.DoSubscriber
import com.alex.willtrip.objectbox.class_boxes.DoDB
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate

class DoManager (private val mutator: DoMutator,
                 private val subscriber: DoSubscriber, private val loader: DoLoader) {

    fun getIdByName(doName:String): Long? = loader.getIdByName(doName)

    fun getAllDo(): List<Do>? = loader.loadAllDo()

    fun getAllExceptArchive(date: LocalDate): List<Do>? = loader.loadAllExceptArchive(date)

    fun getActualDoForDate (date: LocalDate): List<Do>? = loader.loadActualDoForDate(date)

    fun getArchiveDoForDate (date: LocalDate): List<Do>? = loader.loadArchiveDoForDate(date)

    fun getDoById (id: Long): Do? = loader.loadDoById(id)

    fun getDoByName (name: String): Do? = loader.loadDoByName(name)

    fun checkDoNameExists(doName:String): Boolean = loader.checkDoNameExists(doName)

    fun addNewDo (newDo: Do): Long {
        if (checkDoNameExists(newDo.name)) throw
        IllegalArgumentException ("${DoManager::class.java.simpleName}: Do object with the name: ${newDo.name}" +
                "already exists" )
        return mutator.addNewDo(newDo)
    }
    fun removeDo (doId: Long) = mutator.removeDo(doId)

    fun removeDo (doName: String) = mutator.removeDo(doName)

    fun editDo (doName: String, editedDo: Do) = mutator.editDo(doName, editedDo)

    fun editDo (doId: Long, editedDo: Do) = mutator.editDo(doId, editedDo)

    fun addObserver (observer: DataObserver<Class<DoDB>>): DataSubscription = subscriber.addObserver(observer)

    fun removeObserver (dataSubscription: DataSubscription) = subscriber.removeObserver(dataSubscription)
}