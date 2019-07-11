package com.alex.willtrip.ui.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alex.willtrip.core.settings.Setting
import com.alex.willtrip.di.DaggerAppComponent
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscriptionList
import kotlin.concurrent.thread

class SettingViewModel: ViewModel(), DataObserver<Pair<Setting, Int>> {

    private val settingManager = DaggerAppComponent.create().settingsManager()

    private val settingData = MutableLiveData <Pair<Setting,Int>>()

    private val subscriptionList: DataSubscriptionList = DataSubscriptionList()

    fun resetToDefault()= thread { settingManager.resetAllToDefault()}

    fun getData (): MutableLiveData <Pair<Setting,Int>> {

        cancelSubscription()

            Setting.values().forEach {
                val subscription = settingManager.addSettingObserver(it, this)
                subscriptionList.add(subscription)
            }

        return settingData
    }

    private fun cancelSubscription() {
        if (!subscriptionList.isCanceled) subscriptionList.cancel()
    }

    override fun onData(data: Pair<Setting, Int>) {
        settingData.postValue(data)
    }

    fun getCurrentSettingValues(): List<Pair<Setting,Int>> {
        val list = mutableListOf<Pair<Setting,Int>>()
        Setting.values().forEach {
            val value = settingManager.getSettingValue(it)
            list.add(Pair(it, value))
        }
        return list
    }

    fun onSettingChanged(setting: Setting, value: Int) {
        thread {settingManager.setSettingValue(setting, value) }
    }

    override fun onCleared() {
        super.onCleared()
        cancelSubscription()
    }
}