package com.alex.willtrip.core.gratitude.interfaces

import com.alex.willtrip.core.gratitude.Gratitude

interface GratitudeMutator {
    fun addGratitude (gratitude: Gratitude)
    fun removeGratitude (id:Long)
    fun checkGratitudeAlreadyExists (gratitude: Gratitude): Boolean
    fun editGratitude (gratitude: Gratitude)
}