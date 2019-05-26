package com.alex.willtrip.core.story

import com.alex.willtrip.AbstractObjectBoxTest
import com.alex.willtrip.R
import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.story.implementations.ObstacleLoaderImp
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.core.willpower.WPManager
import com.google.common.truth.Truth.assertThat
import com.alex.willtrip.di.DaggerAppComponent
import com.alex.willtrip.di.DaggerEveryDayBehaviorComponent
import io.objectbox.reactive.DataObserver
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

class StoryManagerTest: AbstractObjectBoxTest() {

    lateinit var storyManager: StoryManager
    lateinit var wpManager: WPManager
    lateinit var resultManager: ResultManager
    lateinit var doManager: DoManager
    val currentDate = LocalDate.of(2019, 5,26)

    @Before
    fun setUp() {
        storyManager = DaggerAppComponent.create().storyManager()
        wpManager = DaggerAppComponent.create().wpManager()
        doManager = DaggerAppComponent.create().doManager()
        resultManager = DaggerAppComponent.create().resultManager()
    }

    @Test
    fun checkStoryLoaded() {
        storyManager.loadNewStory(Story())
        val scene = storyManager.getCurrentScene(currentDate)

        val theme = Theme(id=1, link = 1, drawableId = R.drawable.palace, titleTextId = R.string.title_1, titleTintColorId = R.color.colorBlue, soundId = R.raw.king_tube)
        val option =  Option (id = 1, link = 11, nextSceneLink = 2, textId =R.string.option_1)
        val scene1 = Scene (link = 1, theme = theme, sceneTextId = R.string.story_1, obstacles = listOf(), options = listOf(option))

        assertThat(scene).isEqualTo(scene1)
    }

    @Test
    fun isStoryLoaded() {
        val f = storyManager.isStoryLoaded(currentDate)

        storyManager.loadNewStory(Story())
        val t = storyManager.isStoryLoaded(currentDate)

        assertThat(f).isFalse()
        assertThat(t).isTrue()
    }


    @Test
    fun goNextScene() {


        storyManager.loadNewStory(Story())

        val current1 = storyManager.getCurrentScene(currentDate)
        val prev1 = storyManager.getPreviousScene(currentDate)

        val nextSceneLink = current1.options[0].nextSceneLink ?: 1
        storyManager.goNextScene(nextSceneLink, currentDate)

        val current2 = storyManager.getCurrentScene(currentDate)

        val nextSceneLink1 = current2.options[0].nextSceneLink ?: 1

        storyManager.goNextScene(nextSceneLink1, LocalDate.of(2019, 5,26))

        val current3 = storyManager.getCurrentScene(currentDate)
        val prev3 = storyManager.getPreviousScene(currentDate)

        val theme = Theme(id=1, link = 1, drawableId = R.drawable.palace, titleTextId = R.string.title_1, titleTintColorId = R.color.colorBlue, soundId = R.raw.king_tube)
        val theme2 = Theme(id=2, link = 2, drawableId = R.drawable.king, titleTextId = R.string.title_2, titleTintColorId = R.color.colorRed, soundId = R.raw.king)
        val option =  Option (id = 1, link = 11, nextSceneLink = 2, textId =R.string.option_1)
        val option2_1 = Option (id=2, link = 21, nextSceneLink = 3, textId = R.string.option_2_1)
        val option2_2 = Option (id=3, link = 22, nextSceneLink = 3, textId =R.string.option_2_2)
        val option2_3 = Option (id=4, link = 23, nextSceneLink = 3, textId =R.string.option_2_3)
        val option3_1 = Option (id=5, link = 31, nextSceneLink = 4, textId = R.string.option_3_1)
        val option3_2 = Option (id=6, link = 32, nextSceneLink = 4, textId =R.string.option_3_2)

        val scene1 = Scene (link = 1, theme = theme, sceneTextId = R.string.story_1, obstacles = listOf(), options = listOf(option))
        val scene2 = Scene (link = 2, theme = theme2, sceneTextId = R.string.story_2, obstacles = listOf(), options = listOf(option2_1, option2_2, option2_3))
        val scene3 = Scene (link = 3, theme = theme2, sceneTextId = R.string.story_3, obstacles = listOf(), options = listOf(option3_1, option3_2))

        assertThat(current1).isEqualTo(scene1)
        assertThat(prev1).isNull()
        assertThat(current3).isEqualTo(scene3)
        assertThat(prev3).isEqualTo(scene2)
    }

    @Test (expected = IllegalArgumentException::class)
    fun goNextNotExistingScene()  {
        storyManager.loadNewStory(Story())
        storyManager.goNextScene(1111, LocalDate.of(2019, 5,26))
    }

    @Test (expected = IllegalArgumentException::class)
    fun unloadedStoryThrowsException() {
        storyManager.getCurrentScene(currentDate)
    }

    @Test (expected = IllegalArgumentException::class)
    fun notResolvedObstaclesException() {
        storyManager.loadNewStory(Story())

        while (storyManager.getCurrentScene(currentDate).link != 4) {
            val nextSceneLink = storyManager.getCurrentScene(currentDate).options[0].nextSceneLink ?: 1
            storyManager.goNextScene(nextSceneLink, currentDate)
        }

        val nextSceneLink1 = storyManager.getCurrentScene(currentDate).options[0].nextSceneLink ?: 1
        storyManager.goNextScene(nextSceneLink1, currentDate)
    }

    @Test
    fun checkObstaclesResolved() {
        storyManager.loadNewStory(Story())

        val t1 = storyManager.checkObstaclesResolved(currentDate)

        while (storyManager.getCurrentScene(currentDate).link != 4) {
            val nextSceneLink = storyManager.getCurrentScene(currentDate).options[0].nextSceneLink ?: 1
            storyManager.goNextScene(nextSceneLink, currentDate)
        }

        val f1 = storyManager.checkObstaclesResolved(currentDate)
        wpManager.increaseWP(1)


        val t2 = storyManager.checkObstaclesResolved(currentDate)

        assertThat(t1).isTrue()
        assertThat(t2).isTrue()
        assertThat(f1).isFalse()
    }

    @Test
    fun bonusGranted() {
        storyManager.loadNewStory(Story())

        val obstacleLoader = ObstacleLoaderImp(wpManager, doManager, resultManager)
        val obstacle = obstacleLoader.loadObstacle(7, currentDate) as ObstacleBonus
        val f = obstacle.isBonusGranted

        val obstacle7 = ObstacleBonus(link = 7, textId = R.string.obstacle_7, totalValue = 5)
        storyManager.bonusGranted(obstacle7)

        val obstacle1 = obstacleLoader.loadObstacle(7, currentDate) as ObstacleBonus
        val t = obstacle1.isBonusGranted

        assertThat(f).isFalse()
        assertThat(t).isTrue()
    }

    @Test
    fun testObserver() {
        storyManager.loadNewStory(Story())
        val observer = SceneChangeObserver()
        val subscriber = storyManager.addObserver(observer)

        storyManager.goNextScene(2, currentDate)
        storyManager.goNextScene(3, currentDate)

        Thread.sleep(500)
        storyManager.removeObserver(subscriber)
        storyManager.goNextScene(4, currentDate)

        assertThat (observer.linkList).isEqualTo(listOf(1,1,2,2,3))
    }

    class SceneChangeObserver: DataObserver<Int> {
        val linkList = mutableListOf<Int>()
        override fun onData(data: Int) {
            linkList.add(data)
        }
    }

    @Test
    fun goThroughStory() {
        storyManager.loadNewStory(Story())

        while (storyManager.getCurrentScene(currentDate).options[0].nextSceneLink != null) {
            resolveObstacles()

            val nextSceneLink = storyManager.getCurrentScene(currentDate).options[0].nextSceneLink ?: 1
            storyManager.goNextScene(nextSceneLink, currentDate)
        }

        val currentLink = storyManager.getCurrentScene(currentDate).link
        assertThat(currentLink).isEqualTo(53)
    }

    private fun resolveObstacles() {
        if (!storyManager.checkObstaclesResolved(currentDate)) {
            val obstacles = storyManager.getCurrentScene(currentDate).obstacles
            obstacles.forEach {
                when (it) {
                    is ObstacleWP -> {
                        val diff = it.totalValue - wpManager.getCurrentWP()
                        wpManager.increaseWP(diff)
                    }
                    is ObstacleBonus -> {
                        wpManager.increaseWP(it.totalValue)
                        it.isBonusGranted = true
                        storyManager.bonusGranted(it)
                    }
                    is ObstacleComp -> {
                        val doObj1 = Do(
                            name = "Test",
                            periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
                            note = "Test do",
                            isSpecialDayEnabled = true,
                            isPositive = false,
                            complexity = it.totalValue,
                            startDate = LocalDate.of(2019, 4, 3),
                            expireDate = LocalDate.of(2019, 12, 31)
                        )
                        doManager.addNewDo(doObj1)
                    }
                    is ObstacleCount -> {
                        val doCount = doManager.getActualDoForDate(currentDate)?.size ?: 0
                        val diff = it.totalValue - doCount
                        var i = 0
                        while (diff - i > 0) {
                            val doObj1 = Do(
                                name = "Test+1+$i",
                                periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
                                note = "Test do",
                                isSpecialDayEnabled = true,
                                isPositive = false,
                                complexity = 3,
                                startDate = LocalDate.of(2019, 4, 3),
                                expireDate = LocalDate.of(2019, 12, 31)
                            )
                            doManager.addNewDo(doObj1)
                            i++
                        }
                    }
                    is ObstacleChain -> {
                        val doList = doManager.getActualDoForDate(currentDate)
                        if (doList.isNullOrEmpty()) {
                            val doObj1 = Do(
                                name = "Test+X",
                                periodBehavior = DaggerEveryDayBehaviorComponent.create().everyDayBehavior(),
                                note = "Test do",
                                isSpecialDayEnabled = true,
                                isPositive = false,
                                complexity = 2,
                                startDate = LocalDate.of(2019, 4, 3),
                                expireDate = LocalDate.of(2019, 12, 31)
                            )
                            doManager.addNewDo(doObj1)
                        }
                        val doList1 = doManager.getActualDoForDate(currentDate)
                        var maxChain = Pair(0, 0)
                        doList1?.forEach {
                            val chain = resultManager.getMaxChain(it.id)?.length ?: 0
                            if (chain >= maxChain.second) maxChain = Pair(it.id.toInt(), chain)
                        }
                        val diff =  it.totalValue - maxChain.second
                        var i = 0
                        val results = resultManager.getNLastResults(maxChain.first.toLong(), 1000)
                        val date = if (results.isNotEmpty()) results.last().date.minusDays(1) else currentDate
                        while (diff - i >= 0) {
                            val result1 = Result(
                                doId = maxChain.first.toLong(),
                                date = date.minusDays(i.toLong()),
                                isPositive = true,
                                wpPoint = 3,
                                chainPoint = 1
                            )
                            resultManager.addResult(result1)
                            i++
                        }
                    }
                }
            }
        }
    }

    @Test
    fun mapObstaclesResolved() {
        storyManager.loadNewStory(Story())

        while (storyManager.getCurrentScene(currentDate).link != 43) {
            resolveObstacles()

            val nextSceneLink = storyManager.getCurrentScene(currentDate).options[0].nextSceneLink ?: 1
            storyManager.goNextScene(nextSceneLink, currentDate)
        }

        val map = storyManager.mapObstaclesResolved(currentDate)

        val obstacle12 = ObstacleComp(link = 12, textId = R.string.obstacle_12, totalValue = 5)
        obstacle12.doManager = doManager
        val obstacle13 = ObstacleWP(link = 13, textId = R.string.obstacle_13, minValue = 270, addValue = 70, totalValue = 270)
        obstacle13.wpManager = wpManager

        assertThat(map[0].first).isEqualTo(obstacle12)
        assertThat(map[1].first).isEqualTo(obstacle13)
        assertThat(map[0].second).isEqualTo(false)
        assertThat(map[1].second).isEqualTo(false)
        assertThat(map.size).isEqualTo(2)
    }
}