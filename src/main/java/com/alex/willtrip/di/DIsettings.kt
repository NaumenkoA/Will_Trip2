package com.alex.willtrip.di

import com.alex.willtrip.objectbox.class_boxes.SettingsObjectbox
import com.alex.willtrip.core.settings.SettingAccessorImp
import com.alex.willtrip.core.settings.SettingDefaulter
import com.alex.willtrip.core.settings.SettingsManager
import com.alex.willtrip.core.settings.interfaces.SettingAccessor
import com.alex.willtrip.core.settings.interfaces.SettingSubscriber
import com.alex.willtrip.core.settings.interfaces.SettingToDefault
import com.alex.willtrip.core.settings.interfaces.SettingsDB
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [SettingsModule::class])
interface SettingsComponent {
    fun settingsManager(): SettingsManager
}

@Module
class SettingsModule {

    @Singleton
    private val objectbox = SettingsObjectbox()

    @Singleton
    @Provides
    fun providesSettingsManager(accessor: SettingAccessor, subscriber: SettingSubscriber,defaulter: SettingToDefault
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
    fun providesDefault(settingsDB: SettingsDB): SettingToDefault{
        return SettingDefaulter(settingsDB)
    }
}