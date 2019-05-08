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

    override fun loadResultsForPeriod(
        doId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
        exceptType: Result.ResultType
    ): List<Result>? {
        return getResultBox().query().equal(Result_.doId, doId)
            .between(Result_.date, startDate.toEpochDay(), endDate.toEpochDay()).
                notEqual(Result_.resultType, exceptType.uid.toLong()).build().find()
    }

    override fun loadResultForDate(doId: Long, date: LocalDate): Result? {
        return getResultBox().query().equal(Result_.doId, doId).equal(Result_.date, date.toEpochDay()).build()
            .findUnique()
    }

    override fun getNLastResults(doId: Long, numberOfResults: Int, exceptType: Result.ResultType): List<Result> {
       return getResultBox().query().equal(Result_.doId, doId).
           notEqual(Result_.resultType, exceptType.uid.toLong()).
           order(Result_.date, QueryBuilder.DESCENDING).build().find(0, numberOfResults.toLong())
    }

    override fun getLastResult(doId: Long, exceptType: Result.ResultType): Result? {
        val list = getResultBox().query().equal(Result_.doId, doId).notEqual(Result_.resultType, exceptType.uid.toLong()).
                orderDesc(Result_.date).build().find(0, 1)
        return if (list.isNotEmpty()) list[0]
        else null
    }
}
