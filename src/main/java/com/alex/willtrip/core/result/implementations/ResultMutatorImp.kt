package com.alex.willtrip.core.result.implementations

import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.Result_
import com.alex.willtrip.core.result.interfaces.ResultMutator
import com.alex.willtrip.objectbox.ObjectBox
import io.objectbox.Box
import org.threeten.bp.LocalDate


class ResultMutatorImp: ResultMutator {

    private fun getResultBox(): Box<Result> {
        return ObjectBox.boxStore.boxFor(Result::class.java)
    }

    override fun addResult(result: Result) {
        getResultBox().put(result)
    }

    override fun removeAllResults(doId: Long) {
        val resultsToRemove = getResultBox().query().equal(Result_.doId, doId).build().find()
        if (resultsToRemove.isNotEmpty()) getResultBox().remove(resultsToRemove)
    }

    override fun removeResult(result: Result) {
        getResultBox().remove(result)
    }

    override fun removeResult(doId: Long, date: LocalDate) {
        val resultToRemove = getResultBox().query().equal(Result_.doId, doId).
            equal(Result_.date, date.toEpochDay()).build().findUnique()
        if (resultToRemove!=null) getResultBox().remove(resultToRemove)
    }
}