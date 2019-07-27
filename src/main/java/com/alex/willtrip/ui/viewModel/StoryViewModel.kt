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
import android.widget.Toast
import android.R.id.message
import com.alex.willtrip.eventbus.SceneChangedEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class StoryViewModel: ViewModel() {

    val storyManager = DaggerAppComponent.create().storyManager()
    val wpManager = DaggerAppComponent.create().wpManager()
    private val storyData = MutableLiveData <Int>()
    private lateinit var subscription: DataSubscription

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SceneChangedEvent) {
        storyData.value = event.currentSceneLink
    }

    fun getData (): MutableLiveData <Int> {
        EventBus.getDefault().register(this);
        storyData.value = 0
        return storyData
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

    fun grantBonus(bonus: ObstacleBonus): Int {
        return if (!bonus.isBonusGranted) {
            wpManager.increaseWP(bonus.totalValue)
            storyManager.bonusGranted(bonus)
            bonus.totalValue
        } else 0
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this);
    }
}