package com.alex.willtrip.di

import com.alex.willtrip.core.willpower.*
import com.alex.willtrip.objectbox.class_boxes.WPObjectbox
import com.alex.willtrip.core.willpower.interfaces.WPMutator
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [WPModule::class])
interface WPComponent {
    fun wpManager(): WPManager
}

@Module
class WPModule {

    @Singleton
    @Provides
    fun providesWPManager(mutator: WPMutator,
                                   loader: WPObjectbox,
                                   messenger: WPObjectbox): WPManager {
        return WPManager (mutator, loader, messenger)
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