package com.alex.willtrip

import com.alex.willtrip.objectbox.ObjectBox
import org.junit.After
import org.junit.Before

import java.io.File
import java.io.IOException

import io.objectbox.BoxStore

abstract class AbstractObjectBoxTest {
    private var boxStoreDir: File? = null
    private var store: BoxStore? = null

    @Before
    @Throws(IOException::class)
    fun setUpXBoxStore() {
        val tempFile = File.createTempFile("object-store-test", "")
        tempFile.delete()
        boxStoreDir = tempFile
        store = MyObjectBox.builder().directory(boxStoreDir!!).build()
        ObjectBox.boxStore = store as BoxStore
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        if (store != null) {
            store!!.close()
            store!!.deleteAllFiles()
        }
    }

}