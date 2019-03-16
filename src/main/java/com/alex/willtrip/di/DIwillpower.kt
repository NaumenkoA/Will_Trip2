package com.alex.willtrip.di

import com.alex.willtrip.willpower.*
import com.alex.willtrip.objectbox.willpower_db.WPObjectbox
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [WPModule::class])
interface WPComponent {
    fun getWPManager(): WPManager
}

@Module
class WPModule {

    @Singleton
    @Provides
    internal fun providesWPManager(mutator: Mutator,
                                   loader: WPObjectbox,
                                   messenger: WPObjectbox): WPManager {
        return WPManager (mutator, loader, messenger)
    }


    @Singleton
    @Provides
    internal fun providesMutator(): Mutator {
        return Mutator()
    }

    @Singleton
    @Provides
    internal fun providesWillPower(): WillPower {
        return WillPower()
    }

    @Singleton
    @Provides
    internal fun providesWPObjectbox(): WPObjectbox {
        return WPObjectbox()
    }

    }