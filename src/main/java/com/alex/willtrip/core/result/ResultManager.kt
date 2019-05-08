package com.alex.willtrip.core.result

import com.alex.willtrip.core.result.interfaces.*
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class ResultManager (
    private val chainLoader: ChainLoader, private val chainWPCounter: ChainWPCounter,
    private val resultLoader: ResultLoader, private val resultMutator: ResultMutator,
    private val resultSubscriber: ResultSubscriber){

    fun getCurrentChain (doId: Long): CurrentChain? {
        return chainLoader.getCurrentChain(doId)
    }

    fun getMaxChain (doId: Long): MaxChain? {
        return chainLoader.getMaxChain(doId)
    }

    private fun putCurrentChain (currentChain: CurrentChain){
        chainLoader.putCurrentChain(currentChain)
    }

    private fun putMaxChain (maxChain: MaxChain) {
        chainLoader.putMaxChain(maxChain)
    }

    fun countChainWP (doId: Long, doComplexity: Int): Int {
        val currentChain = getCurrentChain(doId) ?: return 0
        return chainWPCounter.countChainWP(doComplexity, currentChain)
    }

    fun loadResultsForPeriod(doId: Long, startDate: LocalDate, endDate: LocalDate, exceptType: Result.ResultType = Result.ResultType.SKIPPED): List<Result>? {
        return resultLoader.loadResultsForPeriod(doId, startDate, endDate, exceptType)
    }

    fun loadResultForDate(doId: Long, date: LocalDate): Result? {
        return resultLoader.loadResultForDate(doId, date)
    }

    fun checkResultExists (doId: Long, date: LocalDate): Boolean {
        val result = loadResultForDate(doId, date)
        return result != null
    }

    fun getNLastResults(doId: Long, numberOfResults: Int, exceptType: Result.ResultType = Result.ResultType.SKIPPED): List<Result> {
        return resultLoader.getNLastResults(doId, numberOfResults, exceptType)
    }

    fun getLastResult(doId: Long, exceptType: Result.ResultType = Result.ResultType.SKIPPED): Result? {
        return resultLoader.getLastResult(doId, exceptType)
    }

   fun addResult(result: Result) {
       if (checkResultExists(result.doId, result.date))
           throw IllegalArgumentException(
               "${ResultManager::class.java.simpleName}: can't add new result, " +
                       "result on date: ${result.date} already exists"
           )

       if (result.resultType != Result.ResultType.DONE) return

       val currentChain =
           getCurrentChain(result.doId) ?: CurrentChain(doId = result.doId, length = 0, startDate = result.date)
       var maxChain = getMaxChain(result.doId) ?: MaxChain(doId = result.doId, length = 0, startDate = result.date)

       if (result.isPositive) {
           currentChain.incrementChain()
           chainLoader.putCurrentChain(currentChain)
           if (currentChain.length > maxChain.length) {
               maxChain = MaxChain(doId = result.doId, length = currentChain.length, startDate = currentChain.startDate)
           }
       } else {
           cancelCurrentChain(result.doId)
           if (maxChain.length > 0 && maxChain.endDate == null) {
               val lastResult = getNLastResults(result.doId, 1)[0]
               maxChain.endDate = lastResult.date
           }
       }
           chainLoader.putMaxChain(maxChain)
           resultMutator.addResult(result)
   }

    private fun cancelCurrentChain(doId: Long) = chainLoader.cancelCurrentChain(doId)

    fun removeAllResults(doId: Long) = resultMutator.removeAllResults(doId)

    fun removeResult(result: Result) {
        val isDeleted = resultMutator.removeResult(result)
        if (!isDeleted) throw IllegalArgumentException ("${ResultManager::class.java.simpleName}: can't make remove operation, " +
                "result with ID: ${result.id} is not found")
    }

    fun addObserver(date: LocalDate, observer: DataObserver<List<Result>>): DataSubscription {
        return resultSubscriber.addObserver(date, observer)
    }

    fun removeResult(doId: Long, date: LocalDate) = resultMutator.removeResult(doId, date)

    fun removeObserver(dataSubscription: DataSubscription) {
        resultSubscriber.removeObserver(dataSubscription)
    }
}