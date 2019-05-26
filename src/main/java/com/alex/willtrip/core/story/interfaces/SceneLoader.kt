package com.alex.willtrip.core.story.interfaces

import com.alex.willtrip.core.story.objects.Scene
import org.threeten.bp.LocalDate

interface SceneLoader {
    fun loadSceneByLink(link: Int, currentDate: LocalDate):Scene
    fun saveCurrentScene(currentScene: Scene)
    fun loadCurrentScene(currentDate: LocalDate): Scene
    fun loadPreviousScene(currentDate: LocalDate): Scene?
    fun savePreviousScene(previousScene: Scene)
}