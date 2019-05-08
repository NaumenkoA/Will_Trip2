package com.alex.willtrip.core.result

import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.di.DaggerAppComponent
import com.google.common.truth.Truth.assertThat
import io.objectbox.reactive.DataObserver
import org.junit.Before
import org.junit.Test

import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class ResultManagerTest: AbstractObjectBoxTest() {


    lateinit var resultManager: ResultManager

    @Before
    fun setUp() {
        resultManager = DaggerAppComponent.builder().build().resultManager()
    }

    @Test
    fun checkChains() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date.minusDays(2), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.minusDays(1), isPositive = true, wpPoint = 3, chainPoint = 2)
        val result3 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 2)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)

        val currentChain = resultManager.getCurrentChain(1)
        val maxChain = resultManager.getMaxChain(1)

        assertThat(currentChain?.length).isEqualTo(3)
        assertThat(maxChain?.length).isEqualTo(3)
        assertThat(currentChain?.startDate).isEqualTo(date.minusDays(2))
        assertThat(maxChain?.startDate).isEqualTo(date.minusDays(2))
        assertThat(maxChain?.endDate).isEqualTo(null)

        val result4 = Result(doId = 1, date = date.plusDays(1), isPositive = true, wpPoint = 0, chainPoint = 0,
            resultType = Result.ResultType.SKIPPED)
        val result5 = Result(doId = 1, date = date.plusDays(2), isPositive = true, wpPoint = 0, chainPoint = 0,
            resultType = Result.ResultType.SPECIAL_DAY)
        resultManager.addResult(result4)
        resultManager.addResult(result5)

        val currentChain1 = resultManager.getCurrentChain(1)
        val maxChain1 = resultManager.getMaxChain(1)

        assertThat(currentChain1?.length).isEqualTo(3)
        assertThat(maxChain1?.length).isEqualTo(3)
        assertThat(currentChain1?.startDate).isEqualTo(date.minusDays(2))
        assertThat(maxChain1?.startDate).isEqualTo(date.minusDays(2))
        assertThat(maxChain1?.endDate).isEqualTo(null)

        val result6 = Result(doId = 1, date = date.plusDays(3), isPositive = false, wpPoint = 3, chainPoint = 1)
        resultManager.addResult(result6)

        val currentChain2 = resultManager.getCurrentChain(1)
        val maxChain2 = resultManager.getMaxChain(1)

        assertThat(currentChain2?.length).isEqualTo(null)
        assertThat(maxChain2?.length).isEqualTo(3)
        assertThat(currentChain2?.startDate).isEqualTo(null)
        assertThat(maxChain2?.startDate).isEqualTo(date.minusDays(2))
        assertThat(maxChain2?.endDate).isEqualTo(date)

        val result7 = Result(doId = 1, date = date.plusDays(4), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result8 = Result(doId = 1, date = date.plusDays(5), isPositive = true, wpPoint = 3, chainPoint = 2)
        val result9 = Result(doId = 1, date = date.plusDays(6), isPositive = true, wpPoint = 3, chainPoint = 2)
        resultManager.addResult(result7)
        resultManager.addResult(result8)
        resultManager.addResult(result9)

        val currentChain3 = resultManager.getCurrentChain(1)
        val maxChain3 = resultManager.getMaxChain(1)

        assertThat(currentChain3?.length).isEqualTo(3)
        assertThat(maxChain3?.length).isEqualTo(3)
        assertThat(currentChain3?.startDate).isEqualTo(date.plusDays(4))
        assertThat(maxChain3?.startDate).isEqualTo(date.minusDays(2))
        assertThat(maxChain3?.endDate).isEqualTo(date)

        val result10 = Result(doId = 1, date = date.plusDays(7), isPositive = true, wpPoint = 3, chainPoint = 2)
        val result11 = Result(doId = 1, date = date.plusDays(8), isPositive = true, wpPoint = 3, chainPoint = 2)

        resultManager.addResult(result10)
        resultManager.addResult(result11)

        val currentChain4 = resultManager.getCurrentChain(1)
        val maxChain4 = resultManager.getMaxChain(1)

        assertThat(currentChain4?.length).isEqualTo(5)
        assertThat(maxChain4?.length).isEqualTo(5)
        assertThat(currentChain4?.startDate).isEqualTo(date.plusDays(4))
        assertThat(maxChain4?.startDate).isEqualTo(date.plusDays(4))
        assertThat(maxChain4?.endDate).isEqualTo(null)

        val result12 = Result(doId = 1, date = date.plusDays(9), isPositive = false, wpPoint = 3, chainPoint = 2)
        resultManager.addResult(result12)

        val currentChain5 = resultManager.getCurrentChain(1)
        val maxChain5 = resultManager.getMaxChain(1)


        assertThat(currentChain5?.length).isEqualTo(null)
        assertThat(maxChain5?.length).isEqualTo(5)
        assertThat(currentChain5?.startDate).isEqualTo(null)
        assertThat(maxChain5?.startDate).isEqualTo(date.plusDays(4))
        assertThat(maxChain5?.endDate).isEqualTo(date.plusDays(8))
    }

    @Test
    fun countChainWP() {
        val zero = resultManager.countChainWP(1, 5)
        assertThat(zero).isEqualTo(0)

        val date = LocalDate.of(2019, 4, 28)

        val result1 = Result(doId = 1, date = date.minusDays(14), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.minusDays(13), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result3 = Result(doId = 1, date = date.minusDays(12), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result4 = Result(doId = 1, date = date.minusDays(11), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result5 = Result(doId = 1, date = date.minusDays(10), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result6 = Result(doId = 1, date = date.minusDays(9), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result7 = Result(doId = 1, date = date.minusDays(8), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result8 = Result(doId = 1, date = date.minusDays(7), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result9 = Result(doId = 1, date = date.minusDays(6), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result10 = Result(doId = 1, date = date.minusDays(5), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result11 = Result(doId = 1, date = date.minusDays(4), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result12 = Result(doId = 1, date = date.minusDays(3), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result13 = Result(doId = 1, date = date.minusDays(2), isPositive = true, wpPoint = 3, chainPoint = 1)
        val result14 = Result(doId = 1, date = date.minusDays(1), isPositive = true, wpPoint = 3, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        val zero1 = resultManager.countChainWP(1, 4)
        assertThat(zero1).isEqualTo(0)

        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)
        resultManager.addResult(result6)
        resultManager.addResult(result7)
        resultManager.addResult(result8)

        val two = resultManager.countChainWP(1, 2)
        val three = resultManager.countChainWP(1, 5)

        assertThat(two).isEqualTo(2)
        assertThat(three).isEqualTo(3)

        resultManager.addResult(result9)
        resultManager.addResult(result10)
        resultManager.addResult(result11)
        resultManager.addResult(result12)
        resultManager.addResult(result13)
        resultManager.addResult(result14)

        val four = resultManager.countChainWP(1, 4)
        val five = resultManager.countChainWP(1, 5)

        assertThat(four).isEqualTo(4)
        assertThat(five).isEqualTo(5)
    }

    @Test (expected = IllegalArgumentException::class)
    fun addResultOnSameDateException() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date, isPositive = false, wpPoint = 3, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
    }

    @Test
    fun checkResultExists() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        resultManager.addResult(result1)

        val f = resultManager.checkResultExists(1, date.minusDays(1))
        val t = resultManager.checkResultExists(1, date)

        assertThat(f).isFalse()
        assertThat(t).isTrue()
    }

    @Test
    fun loadResultForDate() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.plusDays(1), isPositive = false, wpPoint = 3, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        val result = resultManager.loadResultForDate(1, date.plusDays(1))
        val nullResult = resultManager.loadResultForDate(1, date.plusDays(2))

        assertThat(result).isEqualTo(result2)
        assertThat(nullResult).isEqualTo(null)
    }

    @Test
    fun loadResultsForPeriod() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.plusDays(1), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result3 = Result(doId = 1, date = date.plusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result4 = Result(doId = 1, date = date.plusDays(3), isPositive = false, wpPoint = 3, chainPoint = 1, resultType = Result.ResultType.SKIPPED)
        val result5 = Result(doId = 1, date = date.plusDays(4), isPositive = false, wpPoint = 3, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)

        val nullResults = resultManager.loadResultsForPeriod(1, date.minusDays(2), date.minusDays(1))
        val results = resultManager.loadResultsForPeriod(1, date, date.plusDays(3))

        assertThat(nullResults).isEmpty()
        assertThat(results).isEqualTo(listOf(result1, result2, result3))
    }

    @Test
    fun getLastResult() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.plusDays(1), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result3 = Result(doId = 1, date = date.plusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result4 = Result(doId = 1, date = date.minusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result5 = Result(doId = 1, date = date.plusDays(3), resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 3, chainPoint = 1)

        val nullResult = resultManager.getLastResult(1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)

        assertThat(resultManager.getLastResult(1)).isEqualTo(result3)
        assertThat(nullResult).isEqualTo(null)

    }

    @Test
    fun getNLastResults() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.plusDays(1), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result3 = Result(doId = 1, date = date.plusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result4 = Result(doId = 1, date = date.minusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result5 = Result(doId = 1, date = date.plusDays(3), resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 3, chainPoint = 1)

        val nullList = resultManager.getNLastResults(1, 3)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)

        val results = resultManager.getNLastResults(1, 3)

        assertThat(nullList).isEmpty()
        assertThat(results).isEqualTo(listOf(result3, result2, result1))
    }

    @Test
    fun removeResult() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.plusDays(1), isPositive = false, wpPoint = 3, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)

        resultManager.removeResult(result1)
        resultManager.removeResult(1, date.plusDays(1))

        val results = resultManager.getNLastResults(1, 2)

        assertThat(results).isEmpty()
    }

    @Test
    fun removeAllResults() {
        val date = LocalDate.of(2019, 4, 28)
        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 1, date = date.plusDays(1), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result3 = Result(doId = 1, date = date.plusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result4 = Result(doId = 1, date = date.minusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result5 = Result(doId = 1, date = date.plusDays(3), resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 3, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)

        resultManager.removeAllResults(1)

        val result = resultManager.getLastResult(1)

        assertThat(result).isNull()
    }

    @Test
    fun testObserver() {
        val observer = ResultObserver()
        val date = LocalDate.of(2019, 4, 28)

        val subscription = resultManager.addObserver(date, observer)

        val result1 = Result(doId = 1, date = date, isPositive = true, wpPoint = 3, chainPoint = 1)
        val result2 = Result(doId = 2, date = date, isPositive = false, wpPoint = 3, chainPoint = 1)
        val result3 = Result(doId = 3, date = date, isPositive = true, wpPoint = 4, chainPoint = 1)
        val result4 = Result(doId = 1, date = date.minusDays(2), isPositive = false, wpPoint = 3, chainPoint = 1)
        val result5 = Result(doId = 1, date = date.plusDays(3), resultType = Result.ResultType.SKIPPED, isPositive = false, wpPoint = 3, chainPoint = 1)
        val result6 = Result(doId = 4, date = date, isPositive = false, wpPoint = 2, chainPoint = 1)

        resultManager.addResult(result1)
        resultManager.addResult(result2)
        resultManager.addResult(result3)
        resultManager.addResult(result4)
        resultManager.addResult(result5)

        resultManager.removeObserver(subscription)
        Thread.sleep(300)

        resultManager.addResult(result6)

        assertThat(observer.list).isEqualTo(listOf(result1, result2, result3))

    }

    class ResultObserver: DataObserver<List<Result>> {
        lateinit var list: List<Result>

        override fun onData(data: List<Result>) {
            list = data
        }
    }
}