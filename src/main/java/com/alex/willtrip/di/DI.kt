package com.alex.willtrip.di

import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.do_manager.implementations.DoLoaderImp
import com.alex.willtrip.core.do_manager.implementations.DoMutatorImp
import com.alex.willtrip.core.do_manager.implementations.DoSubscriberImp
import com.alex.willtrip.core.do_manager.interfaces.DoLoader
import com.alex.willtrip.core.do_manager.interfaces.DoMutator
import com.alex.willtrip.core.do_manager.interfaces.DoSubscriber
import com.alex.willtrip.core.gratitude.GratitudeManager
import com.alex.willtrip.core.gratitude.implementations.GratitudeDB
import com.alex.willtrip.core.gratitude.implementations.GratitudeWPBonusDB
import com.alex.willtrip.core.gratitude.interfaces.*
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.result.implementations.*
import com.alex.willtrip.core.result.interfaces.*
import com.alex.willtrip.core.settings.SettingAccessorImp
import com.alex.willtrip.core.settings.SettingDefaulter
import com.alex.willtrip.core.settings.SettingsManager
import com.alex.willtrip.core.settings.interfaces.SettingAccessor
import com.alex.willtrip.core.settings.interfaces.SettingSubscriber
import com.alex.willtrip.core.settings.interfaces.SettingToDefault
import com.alex.willtrip.core.settings.interfaces.SettingsDB
import com.alex.willtrip.core.skipped.SkippedResultsManager
import com.alex.willtrip.core.story.StoryManager
import com.alex.willtrip.core.story.implementations.*
import com.alex.willtrip.core.story.interfaces.*
import com.alex.willtrip.core.willpower.Mutator
import com.alex.willtrip.core.willpower.WPManager
import com.alex.willtrip.core.willpower.WillPower
import com.alex.willtrip.core.willpower.interfaces.WPMutator
import com.alex.willtrip.objectbox.class_boxes.SettingsObjectbox
import com.alex.willtrip.objectbox.class_boxes.WPObjectbox
import com.alex.willtrip.objectbox.helpers.DateSaver
import com.alex.willtrip.ui.presenter.SettingPresenter
import com.alex.willtrip.ui.presenter.SettingPresenterImp
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DoManagerModule::class, ResultManagerModule::class, SettingsModule::class,
    WPModule::class, SkippedResultManagerModule::class, StoryModule::class, StoryModule.GratitudeModule::class,
StoryModule.SettingPresenterModule::class])

interface AppComponent {
    fun doManager(): DoManager
    fun resultManager(): ResultManager
    fun settingsManager(): SettingsManager
    fun wpManager(): WPManager
    fun skippedResultsManager(): SkippedResultsManager
    fun storyManager(): StoryManager
    fun gratitudeManager(): GratitudeManager
    fun settingPresenter():SettingPresenter
}

@Module
class DoManagerModule {

    @Singleton
    @Provides
    fun provideDoManager (mutator: DoMutator, loader: DoLoader, subcriber: DoSubscriber): DoManager {
        return DoManager (mutator, subcriber, loader)
    }

    @Singleton
    @Provides
    fun provideMutator(): DoMutator = DoMutatorImp()

    @Singleton
    @Provides
    fun provideLoader(): DoLoader = DoLoaderImp()

    @Singleton
    @Provides
    fun provideSubscriber(): DoSubscriber = DoSubscriberImp()
}

@Module
class ResultManagerModule {

    @Singleton
    @Provides
    fun provideResultManager (chainLoader: ChainLoader, chainWPCounter: ChainWPCounter,
                              mutator: ResultMutator, loader: ResultLoader, subcriber: ResultSubscriber
    ): ResultManager {
        return ResultManager (chainLoader, chainWPCounter, loader, mutator, subcriber)
    }

    @Singleton
    @Provides
    fun provideMutator(): ResultMutator = ResultMutatorImp()

    @Singleton
    @Provides
    fun provideLoader(): ResultLoader = ResultLoaderImp()

    @Singleton
    @Provides
    fun provideSubscriber(): ResultSubscriber = ResultSubscriberImp()

    @Singleton
    @Provides
    fun provideChainLoader(): ChainLoader = ChainLoaderImp()

    @Singleton
    @Provides
    fun provideChainWPCounter(): ChainWPCounter = ChainWPCounterImp()
}

@Module
class SettingsModule {

    @Singleton
    private val objectbox = SettingsObjectbox()

    @Singleton
    @Provides
    fun providesSettingsManager(accessor: SettingAccessor, subscriber: SettingSubscriber, defaulter: SettingToDefault
    ): SettingsManager {
        return SettingsManager (accessor, subscriber, defaulter)
    }


    @Singleton
    @Provides
    fun providesAccessor(settingsDB: SettingsDB): SettingAccessor {
        return SettingAccessorImp(settingsDB)
    }

    @Singleton
    @Provides
    fun providesDB(): SettingsDB {
        return objectbox
    }

    @Singleton
    @Provides
    fun providesSubscriber(): SettingSubscriber {
        return objectbox
    }

    @Singleton
    @Provides
    fun providesDefault(settingsDB: SettingsDB): SettingToDefault {
        return SettingDefaulter(settingsDB)
    }
}

@Module
class WPModule {

    @Singleton
    @Provides
    fun providesWPManager(
        mutator: WPMutator,
        loader: WPObjectbox,
        messenger: WPObjectbox
    ): WPManager {
        return WPManager(mutator, loader, messenger)
    }


    @Singleton
    @Provides
    fun providesMutator(): WPMutator {
        return Mutator()
    }

    @Singleton
    @Provides
    fun providesWillPower(): WillPower {
        return WillPower()
    }

    @Singleton
    @Provides
    fun providesWPObjectbox(): WPObjectbox {
        return WPObjectbox()
    }
}

@Module
class SkippedResultManagerModule {

    @Singleton
    @Provides
    fun providesDateSaver() = DateSaver()

    @Singleton
    @Provides
    fun providesSkippedResultManager(doManager: DoManager,resultManager: ResultManager,
                                     wPManager: WPManager, settingManager: SettingsManager,
                                     dateSaver: DateSaver
    ): SkippedResultsManager = SkippedResultsManager(doManager, resultManager, wPManager, settingManager, dateSaver)
}

@Module
class StoryModule {

    @Singleton
    @Provides
    fun providesStoryManager (obstacleLoader: ObstacleLoader, obstacleResolver: ObstacleResolver,
                              sceneLoader: SceneLoader, subscriber: SceneSubscriber, storyLoader: StoryLoader): StoryManager{
        return StoryManager(obstacleLoader, obstacleResolver, sceneLoader, subscriber, storyLoader)
    }

    @Singleton
    @Provides
    fun providesObstacleLoader (wPManager: WPManager, doManager: DoManager,
                                resultManager: ResultManager): ObstacleLoader{
        return ObstacleLoaderImp (wPManager, doManager, resultManager)
    }

    @Singleton
    @Provides
    fun providesObstacleResolver(): ObstacleResolver {
        return ObstacleResolverImp()
    }

    @Singleton
    @Provides
    fun providesSceneLoader(obstacleLoader: ObstacleLoader): SceneLoader {
        return SceneLoaderImp(obstacleLoader)
    }

    @Singleton
    @Provides
    fun providesSceneSubscriber(): SceneSubscriber {
        return SceneSubscriberImp()
    }

    @Singleton
    @Provides
    fun storyLoader(): StoryLoader {
        return StoryLoaderImp()
    }

    @Module
    class GratitudeModule {

        @Singleton
        @Provides
        fun providesGratitudeManager (wpManager: WPManager, gratitudeLoader: GratitudeLoader, gratitudeMutator: GratitudeMutator,
                                      gratitudeSubscriber: GratitudeSubscriber, gratitudeWPBonusMutator: GratitudeWPBonusMutator,
                                      gratitudeWPBonusSubscriber: GratitudeWPBonusSubscriber): GratitudeManager{
            return GratitudeManager(wpManager, gratitudeLoader, gratitudeMutator, gratitudeSubscriber, gratitudeWPBonusMutator, gratitudeWPBonusSubscriber)
        }

        @Singleton
        @Provides
        fun providesGratitudeLoader(): GratitudeLoader {
            return GratitudeDB()
        }

        @Singleton
        @Provides
        fun providesGratitudeMutator(): GratitudeMutator {
            return GratitudeDB()
        }

        @Singleton
        @Provides
        fun providesGratitudeSubscriber(): GratitudeSubscriber {
            return GratitudeDB()
        }

        @Singleton
        @Provides
        fun providesGratitudeWPBonusMutator(): GratitudeWPBonusMutator {
            return GratitudeWPBonusDB()
        }

        @Singleton
        @Provides
        fun providesGratitudeWPBonusSubscriber(): GratitudeWPBonusSubscriber {
            return GratitudeWPBonusDB()
        }
    }

    @Module
    class SettingPresenterModule {
        @Singleton
        @Provides
        fun providesSettingPresenter (settingManager: SettingsManager):SettingPresenter {
            return SettingPresenterImp(settingManager)
        }
    }
}