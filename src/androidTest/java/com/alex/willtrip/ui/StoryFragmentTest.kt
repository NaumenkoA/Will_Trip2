package com.alex.willtrip.ui

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.alex.willtrip.R
import com.alex.willtrip.core.story.StoryManager
import com.alex.willtrip.di.DaggerAppComponent
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.support.test.runner.AndroidJUnit4
import android.view.View
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.alex.willtrip.core.do_manager.Do
import com.alex.willtrip.core.do_manager.DoManager
import com.alex.willtrip.core.result.Result
import com.alex.willtrip.core.result.ResultManager
import com.alex.willtrip.core.story.objects.*
import com.alex.willtrip.core.willpower.WPManager
import com.alex.willtrip.di.DaggerEveryDayBehaviorComponent
import junit.framework.AssertionFailedError
import org.hamcrest.Matcher
import org.hamcrest.core.IsNot.not
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class StoryFragmentTest {

    private lateinit var storyManager:StoryManager
    private lateinit var resultManager: ResultManager
    private lateinit var doManager: DoManager
    private lateinit var wpManager: WPManager
    private var iterations = 0

    val currentDate = LocalDate.of(2019, 5,26)
    private var resumeStory = true
    private var startedFromBeginning = false

    @Before
    fun prepareTest (){
        createStoryManager()
        resumeStory = true
        startedFromBeginning = false
        iterations = 0
        resultManager = DaggerAppComponent.create().resultManager()
        doManager = DaggerAppComponent.create().doManager()
        wpManager = DaggerAppComponent.create().wpManager()
    }

    private fun createStoryManager() {
        storyManager = DaggerAppComponent.create().storyManager()
    }

    @Test
    fun goThroughStoryTest() {

        startApp()

        onView(withId(R.id.action_trip_mode)).perform(click())

        do {
            createStoryManager()
            if (!isButtonVisible()) {
                onView(isRoot()).perform(waitId(R.id.scenePicture, TimeUnit.SECONDS.toMillis(1)))
                onView(withId(R.id.scenePicture)).perform(click())
            } else {
                if (checkButtonsEnabled()) {
                    val buttonId = getRandomButtonId()
                    onView(withId(buttonId)).perform(click())
                } else {
                    resolveObstacles()
                    onView(withId(R.id.action_settings)).perform(click())
                    onView(withId(R.id.action_trip_mode)).perform(click())
                    val buttonId = getRandomButtonId()
                    onView(withId(buttonId)).perform(click())
                }

                if (storyManager.getCurrentScene(currentDate).options[0].nextSceneLink == null && startedFromBeginning && iterations > 80) {
                   resumeStory = false
                } else if (storyManager.getCurrentScene(currentDate).options[0].nextSceneLink == null) startedFromBeginning = true
            }
        } while (resumeStory)
    }

    private fun getRandomButtonId(): Int {
        createStoryManager()
        val optionCount = storyManager.getCurrentScene(currentDate).options.size
        val randomInteger = (1..optionCount).shuffled().first()

        return when (randomInteger) {
            1 -> R.id.optionButton1
            2 -> R.id.optionButton2
            3 -> R.id.optionButton3
            else -> R.id.optionButton1
        }
    }

    private fun checkButtonsEnabled(): Boolean {
        if (!storyManager.checkObstaclesResolved(currentDate)) {
            when (storyManager.getCurrentScene(currentDate).options.size) {
                1 -> onView(withId(R.id.optionButton1)).check(matches(not(isEnabled())))
                2 -> {
                    onView(withId(R.id.optionButton1)).check(matches(not(isEnabled())))
                    onView(withId(R.id.optionButton2)).check(matches(not(isEnabled())))
                }
                3 -> {
                    onView(withId(R.id.optionButton1)).check(matches(not(isEnabled())))
                    onView(withId(R.id.optionButton2)).check(matches(not(isEnabled())))
                    onView(withId(R.id.optionButton3)).check(matches(not(isEnabled())))
                }
            }
            return false
        } else {
            when (storyManager.getCurrentScene(currentDate).options.size) {
                1 -> onView(withId(R.id.optionButton1)).check(matches(isEnabled()))
                2 -> {
                    onView(withId(R.id.optionButton1)).check(matches(isEnabled()))
                    onView(withId(R.id.optionButton2)).check(matches(isEnabled()))
                }
                3-> {
                    onView(withId(R.id.optionButton1)).check(matches(isEnabled()))
                    onView(withId(R.id.optionButton2)).check(matches(isEnabled()))
                    onView(withId(R.id.optionButton3)).check(matches(isEnabled()))
                }
            }
            return true
        }
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
                            name = "Test + ${wpManager.getCurrentWP()}",
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

    private fun waitId(viewId: Int, waitTime: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view with id <$viewId> during $waitTime millis."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + waitTime
                val viewMatcher = withId(viewId)

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }

    private fun isButtonVisible(): Boolean {
        return try {
            onView(withId(R.id.optionButton1)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            true
        } catch (e: AssertionFailedError) {
            false
        }
    }

    private fun startApp() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val allAppsButton = device.findObject(
            UiSelector().description("Will Trip")
        )
        allAppsButton.clickAndWaitForNewWindow()
    }
}