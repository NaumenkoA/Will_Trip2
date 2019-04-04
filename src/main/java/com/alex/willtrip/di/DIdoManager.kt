package com.alex.willtrip.di

import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.do_manager.implementations.DoLoaderImp
import com.alex.willtrip.core.do_manager.implementations.DoMutatorImp
import com.alex.willtrip.core.do_manager.implementations.DoSubscriberImp
import com.alex.willtrip.core.do_manager.interfaces.DoLoader
import com.alex.willtrip.core.do_manager.interfaces.DoMutator
import com.alex.willtrip.core.do_manager.interfaces.DoSubscriber
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DoManagerModule::class])
interface DoManagerComponent {
    fun doManager(): DoManager
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
    fun provideMutator():DoMutator = DoMutatorImp()

    @Singleton
    @Provides
    fun provideLoader():DoLoader = DoLoaderImp()

    @Singleton
    @Provides
    fun provideSubscriber():DoSubscriber = DoSubscriberImp()
}