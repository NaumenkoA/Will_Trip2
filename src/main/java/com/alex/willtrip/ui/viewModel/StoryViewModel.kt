package com.alex.willtrip.ui.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.alex.willtrip.core.story.objects.Obstacle
import com.alex.willtrip.core.story.objects.ObstacleBonus
import com.alex.willtrip.core.story.objects.Scene
import com.alex.willtrip.core.story.objects.Story
import com.alex.willtrip.di.DaggerAppComponent
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription
import org.threeten.bp.LocalDate
import kotlin.concurrent.thread

class StoryViewModel: ViewModel(), DataObserver<Int> {

    private val storyManager = DaggerAppComponent.create().storyManager()
    private val storyData = MutableLiveData <Int>()
    private lateinit var subscription: DataSubscription

    fun getData (): MutableLiveData <Int> {
        cancelSubscription()
        storyManager.addObserver(this)
        return storyData
    }

    override fun onData(data: Int) {
        storyData.value = data
    }

    fun goNextScene (link: Int, currentDate: LocalDate) {
        thread { storyManager.goNextScene(link, currentDate) }
    }

    fun checkObstaclesResolved (currentDate: LocalDate): Boolean = storyManager.checkObstaclesResolved(currentDate)

    fun mapObstaclesResolved (currentDate: LocalDate): List<Pair<Obstacle, Boolean>> = storyManager.mapObstaclesResolved(currentDate)

    fun loadNewStory () = storyManager.loadNewStory(Story())

    fun isStoryLoaded (currentDate: LocalDate) = storyManager.isStoryLoaded(currentDate)

    fun getCurrentScene (currentDate: LocalDate): Scene = storyManager.getCurrentScene(currentDate)

    fun getPreviousScene(currentDate: LocalDate): Scene? = storyManager.getPreviousScene(currentDate)

    fun bonusGranted (bonus: ObstacleBonus) = storyManager.bonusGranted(bonus)

    private fun cancelSubscription() {
        if (!::subscription.isInitialized) return
        if (!subscription.isCanceled) subscription.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cancelSubscription()
    }
}