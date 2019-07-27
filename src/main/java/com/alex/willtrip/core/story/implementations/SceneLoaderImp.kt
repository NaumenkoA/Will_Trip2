package com.alex.willtrip.core.story.implementations

import com.alex.willtrip.core.story.interfaces.ObstacleLoader
import com.alex.willtrip.core.story.interfaces.SceneLoader
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.eventbus.SceneChangedEvent
import com.alex.willtrip.extensions.toObstacle
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.ObstacleDB
import com.alex.willtrip.objectbox.class_boxes.ObstacleDB_
import com.alex.willtrip.objectbox.helpers.IntSaver
import com.alex.willtrip.objectbox.helpers.IntSaver_
import io.objectbox.Box
import org.greenrobot.eventbus.EventBus
import org.threeten.bp.LocalDate
import java.lang.IllegalArgumentException

class SceneLoaderImp (val obstacleLoader: ObstacleLoader): SceneLoader {

    private fun getIntBox (): Box<IntSaver> {
        return ObjectBox.boxStore.boxFor(IntSaver::class.java)
    }

    private fun getThemeBox (): Box<Theme> {
        return ObjectBox.boxStore.boxFor(Theme::class.java)
    }

    private fun getSceneStubBox (): Box<SceneStub> {
        return ObjectBox.boxStore.boxFor(SceneStub::class.java)
    }

    private fun getOptionBox (): Box<Option> {
        return ObjectBox.boxStore.boxFor(Option::class.java)
    }

    private fun getObstacleDBBox (): Box<ObstacleDB> {
        return ObjectBox.boxStore.boxFor(ObstacleDB::class.java)
    }

    override fun loadSceneByLink(link: Int, currentDate: LocalDate): Scene {
        val sceneStub = getSceneStubBox().query().equal(SceneStub_.link, link.toLong()).build().findUnique() ?: throw IllegalArgumentException ("Scene with link: $link was not found")

        val theme = getThemeBox().query().equal(Theme_.link, sceneStub.themeLink.toLong()).build().findUnique() ?: throw IllegalArgumentException ("Theme with link: ${sceneStub.themeLink} was not found")

        val optionList = mutableListOf<Option>()
        sceneStub.optionLinkArray.forEach {
            val option = getOptionBox().query().equal(Option_.link, it.toLong()).build().findUnique() ?: throw IllegalArgumentException ("Option with link: $it was not found")
            optionList.add(option)
        }

        val obstacleList = mutableListOf<Obstacle>()
        sceneStub.obstacleLinkArray.forEach {
            val obstacle = obstacleLoader.loadObstacle(it, currentDate)
            obstacleList.add(obstacle)
        }

        return Scene(sceneStub.link, theme, sceneStub.mainTextId, obstacleList, optionList)
    }

    override fun saveCurrentScene(currentScene: Scene) {
        val id = getIntBox().query().equal(IntSaver_.link, 1).build().findUnique()?.id ?: 0
        getIntBox().put(IntSaver(id = id, link = 1, value = currentScene.link))
        EventBus.getDefault().post(SceneChangedEvent(currentScene.link))
    }

    override fun savePreviousScene(previousScene: Scene) {
        val id = getIntBox().query().equal(IntSaver_.link, 2).build().findUnique()?.id ?: 0
        getIntBox().put(IntSaver(id= id, link = 2, value = previousScene.link))
    }

    override fun loadCurrentScene(currentDate: LocalDate): Scene {
        val lastSceneLink = getIntBox().query().equal(IntSaver_.link, 1).build().findUnique()?.value ?: 1
        return loadSceneByLink(lastSceneLink, currentDate)
    }

    override fun loadPreviousScene(currentDate: LocalDate): Scene? {
        val previousSceneLink = getIntBox().query().equal(IntSaver_.link, 1).build().findUnique() ?: return null
        return loadSceneByLink(previousSceneLink.value, currentDate)
    }
}