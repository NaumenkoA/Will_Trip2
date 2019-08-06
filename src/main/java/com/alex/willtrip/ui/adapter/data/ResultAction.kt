package com.alex.willtrip.ui.adapter.data

import kotlin.reflect.jvm.internal.impl.renderer.DescriptorRenderer

class ResultAction (val doId: Long, val name: String, val type: String, val complexity: Int, val chainWP: Int, val note: String) {

    val totalWP
        get() = complexity + chainWP

    companion object {
        const val SUCCESS = "success"
        const val FAIL = "fail"
        const val SKIPPED = "skipped"
        const val SPECIAL_DAY = "special_day"
        const val DEFAULT = "default"
    }
}