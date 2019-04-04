package com.alex.willtrip.core.result.implementations

import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.Result_
import com.alex.willtrip.core.result.interfaces.ResultLoader
import com.alex.willtrip.objectbox.ObjectBox
import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import org.threeten.bp.LocalDate

class ResultLoaderImp: ResultLoader {

    private fun getResultBox(): Box<Result> {
        return ObjectBox.boxStore.boxFor(Result::class.java)
    }

    override fun loadResultsForPeriod(doId: Long, startDate: LocalDate, endDate: LocalDate): List<Result>? {
        return getResultBox().query().equal(Result_.doId, doId)
            .between(Result_.date, startDate.toEpochDay(), endDate.toEpochDay()).build().find()
    }

    override fun loadResultForDate(doId: Long, date: LocalDate): Result? {
        return getResultBox().query().equal(Result_.doId, doId).equal(Result_.date, date.toEpochDay()).build()
            .findUnique()
    }

    override fun getLastResults(doId: Long, numberOfResults: Int): List<Result> {
        val allResults =
            getResultBox().query().equal(Result_.doId, doId).order(Result_.date, QueryBuilder.DESCENDING).build().find()

        return if (allResults.size <= numberOfResults) allResults
        else allResults.subList(0, numberOfResults)
    }

    override fun getLastNonSkippedResults(doId: Long, numberOfResults: Int): List<Result> {
        val allResults =
            getResultBox().query().equal(Result_.doId, doId).notEqual(Result_.resultType, Result.ResultType.SKIPPED.name).order(Result_.date, QueryBuilder.DESCENDING).build().find()

        return if (allResults.size <= numberOfResults) allResults
        else allResults.subList(0, numberOfResults)
    }

}
