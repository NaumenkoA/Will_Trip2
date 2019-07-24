package com.alex.willtrip.core.story.implementations

import com.alex.willtrip.core.story.interfaces.StoryLoader
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.extensions.convertToObstacleDBList
import com.alex.willtrip.extensions.toObstacleDB
import com.alex.willtrip.objectbox.ObjectBox
import com.alex.willtrip.objectbox.class_boxes.ObstacleDB
import io.objectbox.Box

class StoryLoaderImp: StoryLoader {

    private fun getThemeBox (): Box<Theme> {
        return ObjectBox.boxStore.boxFor(Theme::class.java)
    }

    private fun getSceneStubBox (): Box<SceneStub> {
        return ObjectBox.boxStore.boxFor(SceneStub::class.java)
    }

    private fun getOptionBox (): Box<Option> {
        return ObjectBox.boxStore.boxFor(Option::class.java)
    }

    private fun getObstacleBox (): Box<ObstacleDB> {
        return ObjectBox.boxStore.boxFor(ObstacleDB::class.java)
    }

    override fun loadStory(story: Story) {
        clean()
        getThemeBox().put(story.themeList)
        getSceneStubBox().put(story.sceneList)
        getOptionBox().put(story.optionList)
        val obstacleDBList = story.obstacleList.convertToObstacleDBList()
        getObstacleBox().put(obstacleDBList)
    }

    private fun clean() {
        getThemeBox().removeAll()
        getSceneStubBox().removeAll()
        getOptionBox().removeAll()
        getObstacleBox().removeAll()
    }
}


