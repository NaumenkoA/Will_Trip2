//package com.alex.willtrip.core.result.implementations
//
//import com.alex.willtrip.core.do_manager.implementations.DoLoaderImp
//import com.alex.willtrip.core.do_manager.implementations.LongSaver_
//import com.alex.willtrip.core.result.interfaces.SkippedResultsChecker
//import com.alex.willtrip.core.willpower.WPManager
//import com.alex.willtrip.objectbox.ObjectBox
//import io.objectbox.Box
//import io.objectbox.annotation.Entity
//import io.objectbox.annotation.Id
//import io.objectbox.annotation.Index
//import org.threeten.bp.LocalDate
//
//class SkippedResultsCheckerImp (val doLoader: DoLoaderImp, val resultLoader: ResultLoaderImp,
//                                val WPManager: WPManager):
//    SkippedResultsChecker {
//
//    private fun getLongSaverBox(): Box<LongSaver> {
//        return ObjectBox.boxStore.boxFor(LongSaver::class.java)
//    }
//
//    private val lastDateChecked = getLongSaverBox().query().equal(LongSaver_.link, 1001).
//        build().findUnique()?.value ?: getEarliestDoStartDate()
//
//
//    override fun checkSkippedResults(checkDate: LocalDate): Triple<LocalDate?, LocalDate?, Int?> {
//        if (lastDateChecked == null) {
//            return Triple(null, null, null)
//        }
//        var date = LocalDate.ofEpochDay(lastDateChecked)
//
//        var startDate: LocalDate? = null
//        var endDate: LocalDate? = null
//        var lostWPValue: Int? = null
//
//        date = date.plusDays(1)
//        while (date <= checkDate){
//            val doListForDate = doLoader.getActualDoForDate(date)
//            doListForDate?.forEach {
//                if (it.isObligatoryOnDate(date))
//            }
//
//        }
//    }
//
//    private fun getEarliestDoStartDate(): Long? {
//        val doList = doLoader.getAllDo() ?: return null
//        doList.sortedBy { it.startDate }
//        return doList[0].startDate.toEpochDay()
//    }
//}
//
//@Entity
//class LongSaver {
//    @Id
//    var id: Long = 0
//
//    @Index
//    val link: Int = 1001
//
//    var value: Long = 0
//}