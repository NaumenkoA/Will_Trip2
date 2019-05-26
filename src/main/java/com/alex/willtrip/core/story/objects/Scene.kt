package com.alex.willtrip.core.story.objects

data class Scene (val link: Int, val theme: Theme, val sceneTextId: Int,
             val obstacles: List<Obstacle>, val options: List<Option>)