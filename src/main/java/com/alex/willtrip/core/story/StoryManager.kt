package com.alex.willtrip.core.story

import com.alex.willtrip.core.story.interfaces.*
import com.alex.willtrip.core.story.objects.*
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class StoryManager (
    private val obstacleLoader: ObstacleLoader, private val obstacleResolver: ObstacleResolver, private val sceneLoader: SceneLoader,
    private val subscriber: SceneSubscriber, private val storyLoader: StoryLoader) {

    private lateinit var currentScene: Scene

    private var previousScene: Scene? = null

    private fun saveObstacle(obstacle: Obstacle){
        return obstacleLoader.saveObstacle(obstacle)
    }

    private fun switchScene (newSceneLink: Int, currentDate: LocalDate): Scene {
        checkCurrentSceneInitialized(currentDate)

        val newScene = sceneLoader.loadSceneByLink(newSceneLink, currentDate)
        previousScene = currentScene
        currentScene = newScene

        sceneLoader.savePreviousScene(previousScene!!)
        sceneLoader.saveCurrentScene(currentScene)

        return currentScene
    }

    fun goNextScene (link: Int, currentDate: LocalDate): Scene {
        checkCurrentSceneInitialized(currentDate)

        if (!checkObstaclesResolved(currentDate)) throw IllegalArgumentException ("${StoryManager::class.java.simpleName}+" +
                ": error - trying go next scene in the story, when not all obstacles are resolved on stage with link:${currentScene.link}")
        return switchScene(link, currentDate)
    }

    private fun checkCurrentSceneInitialized(currentDate: LocalDate) {
        if (!this::currentScene.isInitialized) getCurrentScene(currentDate)
    }

    fun checkObstaclesResolved (currentDate: LocalDate): Boolean {
        checkCurrentSceneInitialized(currentDate)
        return obstacleResolver.checkAllObstaclesResolved(currentDate, currentScene.obstacles)
    }

    fun mapObstaclesResolved (currentDate: LocalDate): List<Pair<Obstacle, Boolean>> {
        checkCurrentSceneInitialized(currentDate)
        return obstacleResolver.mapObstaclesResolved(currentDate, currentScene.obstacles)
    }

    fun loadNewStory (story: Story) {
        storyLoader.loadStory(story)

    }

    fun isStoryLoaded (currentDate: LocalDate):Boolean {
        try {
            sceneLoader.loadCurrentScene(currentDate)
        } catch (e: IllegalArgumentException) {
            return false
        }
        return true
    }

    fun getCurrentScene(currentDate: LocalDate): Scene {
        if (!this::currentScene.isInitialized) {
            currentScene = sceneLoader.loadCurrentScene(currentDate)
        }
        return currentScene
    }

    fun getPreviousScene(currentDate: LocalDate): Scene? {
        if (previousScene == null) {
            previousScene = sceneLoader.loadPreviousScene(currentDate)
        }
        return previousScene
    }

    fun bonusGranted (bonus: ObstacleBonus) {
        bonus.isBonusGranted = true
        saveObstacle(bonus)
    }

    fun addObserver (observer: DataObserver<Int>): DataSubscription {
        return subscriber.addObserver(observer)
    }

    fun removeObserver (dataSubscription: DataSubscription) = subscriber.removeObserver(dataSubscription)
}