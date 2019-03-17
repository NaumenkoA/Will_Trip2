package com.alex.willtrip.objectbox.class_boxes

import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.settings.Setting
import com.alex.willtrip.settings.interfaces.SettingSubscriber
import com.alex.willtrip.settings.interfaces.SettingsDB
import io.objectbox.Box
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class SettingsObjectbox: SettingsDB, SettingSubscriber {

    private fun getBox (): Box<SettingEntity> {
        return ObjectBox.boxStore.boxFor(SettingEntity::class.java)
    }

    override fun saveSetting(settingName: String, settingValue: Int) {
        val query = getBox().query().equal(SettingEntity_.name, settingName).build()
        val id = query.findUnique()?.id ?: 0
        getBox().put(SettingEntity(id, settingName, settingValue))
    }

    override fun readSettingOrDefault(settingName: String): Int {
        val query = getBox().query().equal(SettingEntity_.name, settingName).build()
        val setting = Setting.valueOf(settingName)
        return query.findUnique()?.value ?: setting.default
    }

    override fun addObserver(settingName: String, observer: DataObserver<Pair<String, Int>>): DataSubscription {
        val query = getBox().query().equal(SettingEntity_.name, settingName).build()
        return query.subscribe().
            transform {
                val settingEntity = query.findUnique()
                val setting = Setting.valueOf(settingName)
                val settingValue = settingEntity?.value ?: setting.default
                Pair (setting.name, settingValue)}.observer(observer)
    }

    override fun removeObserver(dataSubscription: DataSubscription) {
        if (!dataSubscription.isCanceled) dataSubscription.cancel()
    }
}

    @Entity
    class SettingEntity (@Id var id: Long = 0, @Index val name: String, val value: Int)