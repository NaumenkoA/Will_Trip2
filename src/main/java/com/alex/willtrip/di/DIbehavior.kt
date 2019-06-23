package com.alex.willtrip.di

import com.alex.willtrip.core.do_manager.period.*
import com.alex.willtrip.core.result.implementations.ResultLoaderImp
import com.alex.willtrip.core.result.interfaces.ResultLoader
import com.alex.willtrip.core.settings.SettingsManager
import dagger.Component
import dagger.Module
import dagger.Provides
import org.threeten.bp.LocalDate
import javax.inject.Scope

@Component (modules = [ResultLoaderModule::class, SingleBehaviorModule::class])
interface SingleBehaviorComponent  {
    fun singleBehavior(): SingleBehavior
}

@Component (modules = [ResultLoaderModule::class, DaysOfWeekBehaviorModule::class])
    interface DaysOfWeekBehaviorComponent  {
        fun daysOfWeekBehavior(): DaysOfWeekBehavior
    }

    @Component (modules = [ResultLoaderModule::class, EveryDayBehaviorModule::class])
    interface EveryDayBehaviorComponent  {
        fun everyDayBehavior(): EveryDayBehavior
    }

    @BehaviorScope
    @Component (dependencies = [AppComponent::class],
        modules = [ResultLoaderModule::class, EveryNDaysBehaviorModule::class])
    interface EveryNDaysBehaviorComponent  {
        fun everyNDaysBehavior(): EveryNDaysBehavior
    }

    @Scope
    annotation class BehaviorScope

    @Component (modules = [ResultLoaderModule::class, NTimesAWeekBehaviorModule::class])
    interface NTimesAWeekBehaviorComponent  {
        fun nTimesAWeekBehavior(): NTimesAWeekBehavior
    }

    @Component (modules = [ResultLoaderModule::class, NTimesAMonthBehaviorModule::class])
    interface NTimesAMonthBehaviorComponent  {
        fun nTimesAMonthBehavior(): NTimesAMonthBehavior
    }

    @Module
    class ResultLoaderModule {
        @Provides
        fun providesResultLoader(): ResultLoader = ResultLoaderImp()
    }

    @Module
    class SingleBehaviorModule (val date: LocalDate) {
        @Provides
        fun provideDaysOfWeekBehavior (resultLoader: ResultLoader): SingleBehavior{
            return SingleBehavior(date, resultLoader)
        }
    }

    @Module
    class DaysOfWeekBehaviorModule (val daysList: List<Int>) {
        @Provides
        fun provideDaysOfWeekBehavior (resultLoader: ResultLoader): DaysOfWeekBehavior{
            return DaysOfWeekBehavior(daysList, resultLoader)
        }
    }

    @Module
    class EveryDayBehaviorModule {
        @Provides
        fun provideEveryDayBehavior(resultLoader: ResultLoader): EveryDayBehavior {
            return EveryDayBehavior(resultLoader)
        }
    }

    @Module
    class EveryNDaysBehaviorModule (val repeatPeriod: Int) {
        @Provides
        fun provideEveryNDaysBehavior (resultLoader: ResultLoader,
                                       settingsManager: SettingsManager): EveryNDaysBehavior{
            return EveryNDaysBehavior(repeatPeriod, resultLoader, settingsManager)
        }
    }

    @Module
    class NTimesAWeekBehaviorModule (val timesAWeek: Int) {
        @Provides
        fun provideNTimesAWeekBehavior(resultLoader: ResultLoader): NTimesAWeekBehavior {
            return NTimesAWeekBehavior(timesAWeek, resultLoader)
        }
    }

    @Module
    class NTimesAMonthBehaviorModule(val timesAMonth: Int) {
        @Provides
        fun provideNTimesAMonthBehavior(resultLoader: ResultLoader): NTimesAMonthBehavior {
            return NTimesAMonthBehavior(timesAMonth, resultLoader)
        }
    }


