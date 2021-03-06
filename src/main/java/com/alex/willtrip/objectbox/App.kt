package com.alex.willtrip.objectbox

import android.app.Application
import com.alex.willtrip.core.MyObjectBox

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBox.boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}