package com.alex.willtrip.di

import com.alex.willtrip.objectbox.class_boxes.SettingsObjectbox
import com.alex.willtrip.settings.SettingAccessorImp
import com.alex.willtrip.settings.SettingDefaulter
import com.alex.willtrip.settings.SettingsManager
import com.alex.willtrip.settings.interfaces.SettingAccessor
import com.alex.willtrip.settings.interfaces.SettingSubscriber
import com.alex.willtrip.settings.interfaces.SettingToDefault
import com.alex.willtrip.settings.interfaces.SettingsDB
import com.alex.willtrip.willpower.interfaces.WPSubscriber
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
    internal fun providesSettingsManager(accessor: SettingAccessor, subscriber: SettingSubscriber,defaulter: SettingToDefault
    ): SettingsManager {
        return SettingsManager (accessor, subscriber, defaulter)
    }


    @Singleton
    @Provides
    internal fun providesAccessor(settingsDB: SettingsDB): SettingAccessor {
        return SettingAccessorImp(settingsDB)
    }

    @Singleton
    @Provides
    internal fun providesDB(): SettingsDB {
        return objectbox
    }

    @Singleton
    @Provides
    internal fun providesSubscriber(): SettingSubscriber {
        return objectbox
    }

    @Singleton
    @Provides
    internal fun providesDefault(settingsDB: SettingsDB): SettingToDefault{
        return SettingDefaulter(settingsDB)
    }
}