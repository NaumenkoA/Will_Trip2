package com.alex.willtrip.ui


import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.alex.willtrip.R
import com.alex.willtrip.core.story.objects.Scene
import com.alex.willtrip.ui.viewModel.StoryViewModel
import kotlinx.android.synthetic.main.fragment_story.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class StoryFragment : Fragment(), Observer<Int> {

    private lateinit var viewModel:StoryViewModel
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var buttonArray: ArrayList <Button>
    private lateinit var currentScene: Scene
    private var previousScene:Scene? = null
    private var isLoading = false
    private val matrix = ColorMatrix()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =  ViewModelProviders.of(this).get(StoryViewModel::class.java)
        if (!viewModel.isStoryLoaded(getCurrentDate())) viewModel.loadNewStory()
        viewModel.getData().observe(this, this)

        // options are reversed on buttons for better perception
        optionButton1.setOnClickListener {
            goNextScene(1)
        }
        optionButton2.setOnClickListener {
            goNextScene(2)
        }
        optionButton3.setOnClickListener {
            goNextScene(3)
        }

        buttonArray = arrayListOf(optionButton1, optionButton2, optionButton3)

        matrix.setSaturation(1f)

        showLoading()
    }

    override fun onResume() {
        super.onResume()

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun goNextScene(buttonNumber: Int) {
        val selectedOptionPozition = currentScene.options.size - buttonNumber
        val link = currentScene.options[selectedOptionPozition].nextSceneLink

        if (link != null) viewModel.goNextScene(link, getCurrentDate())
        else viewModel.loadNewStory()
    }

    override fun onChanged(link: Int?) {
        previousScene = viewModel.getPreviousScene(getCurrentDate())
        currentScene = viewModel.getCurrentScene(getCurrentDate())
        val theme = currentScene.theme

        if (currentScene.theme != previousScene?.theme) {
            emptyScreen()
            stopSound()
            if (isLoading) hideLoading()
            playSound(theme.soundId)
            showBackground(theme.drawableId, theme.titleTextId, theme.titleTintColorId, true)
        } else {
            stopSound()
            if (isLoading) hideLoading()
            showBackground(theme.drawableId, theme.titleTextId, theme.titleTintColorId, false)
            prepareForeground(false)
            showSceneContent(currentScene, false)
        }
    }

    private fun getCurrentDate(): LocalDate {
        val currentDate = Calendar.getInstance().time
        return Instant.ofEpochMilli(currentDate.time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun emptyScreen() {
        hidingImageView.alpha = 0f
        titleTextView.visibility = View.INVISIBLE
        stageTextView.visibility = View.INVISIBLE
        obstacleTextView.visibility = View.INVISIBLE
        optionButton1.visibility = View.INVISIBLE
        optionButton2.visibility = View.INVISIBLE
        optionButton3.visibility = View.INVISIBLE
    }

    private fun showBackground(drawableId: Int, titleTextId: Int, titleTintColorId: Int, isAnimated: Boolean) {
        scenePicture.setImageResource(drawableId)
        setFragmentBackgroundColor(drawableId)
        matrix.setSaturation(1f)
        scenePicture.colorFilter = ColorMatrixColorFilter(matrix)

        titleTextView.visibility = View.VISIBLE
        titleTextView.text = context?.getString(titleTextId)
        titleTextView.background.setTint(ContextCompat.getColor(context!!, titleTintColorId))

        hidingImageView.bringToFront()
        hidingImageView.setOnClickListener {
            prepareForeground(true)
        }

        titleTextView.setOnClickListener {
            prepareForeground(true)
        }

        if (isAnimated) {
            val titleFadeInAnimator = ObjectAnimator.ofFloat(titleTextView, "alpha", 0f, 1f)
            titleFadeInAnimator.duration = 2000
            val imageFadeInAnimator = ObjectAnimator.ofFloat(scenePicture, "alpha", 0f, 1f)
            imageFadeInAnimator.duration = 2000
            val animatorSet = AnimatorSet()
            animatorSet.play(titleFadeInAnimator).with(imageFadeInAnimator)
            animatorSet.start()
        }
    }

    private fun setFragmentBackgroundColor(drawableId: Int) {
        val bitmap = BitmapFactory.decodeResource(context!!.resources, drawableId)
        val palette = Palette.from(bitmap).generate()
        val color = palette.getLightVibrantColor(ContextCompat.getColor(context!!, R.color.colorGrey))
        this.view!!.setBackgroundColor(color)
    }

    private fun prepareForeground(isAnimated: Boolean) {

        titleTextView.setOnClickListener {  }
        hidingImageView.setOnClickListener {  }
        titleTextView.bringToFront()

        if (isAnimated) {
            val saturationAnimator = ObjectAnimator.ofFloat(matrix, "saturation", 1f, 0f)
            saturationAnimator.duration = 1000
            saturationAnimator.addUpdateListener {
                val filter = ColorMatrixColorFilter(matrix)
                scenePicture.colorFilter = filter
            }

            val backgroundAnimator = ObjectAnimator.ofFloat(hidingImageView, "alpha", 0f, 1f)
            backgroundAnimator.duration = 1000
            backgroundAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    showSceneContent(currentScene, true)
                }
                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(saturationAnimator, backgroundAnimator)
            animatorSet.start()
        } else {
            matrix.setSaturation(0f)
            scenePicture.colorFilter = ColorMatrixColorFilter (matrix)
            hidingImageView.alpha = 1f
        }
    }

    private fun playSound(soundId: Int) {
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()
    }

    private fun showSceneContent(mainText: String, obstacleList: List<String>, optionList: List<String>, isOptionEnabled: Boolean, isAnimated: Boolean) {
        contentLayout.invalidate()
        stageTextView.visibility = View.VISIBLE
        scrollView.bringToFront()
        buttonArray.forEachIndexed { index, button ->
            val i = optionList.size - 1 - index
            setButtonState(button, optionList.getOrNull(i), isOptionEnabled)
        }

        stageTextView.text = mainText
        var obstacleText = ""
        obstacleList.forEach {
            obstacleText += it +"\n"
        }
        if (obstacleText == "") obstacleTextView.visibility = View.GONE
        else{
            obstacleTextView.visibility = View.VISIBLE
            obstacleTextView.text = obstacleText
        }

        if (isAnimated) {
            val stageTextFadeInAnimator = ObjectAnimator.ofFloat(stageTextView, "alpha", 0f, 1f)
            stageTextFadeInAnimator.duration = 1000
            val obstacleTextFadeInAnimator = ObjectAnimator.ofFloat(obstacleTextView, "alpha", 0f, 1f)
            obstacleTextFadeInAnimator.duration = 1000
            val button1FadeInAnimator = ObjectAnimator.ofFloat(optionButton1, "alpha", 0f, 1f)
            button1FadeInAnimator.duration = 1000
            val button2FadeInAnimator = ObjectAnimator.ofFloat(optionButton2, "alpha", 0f, 1f)
            button2FadeInAnimator.duration = 1000
            val button3FadeInAnimator = ObjectAnimator.ofFloat(optionButton3, "alpha", 0f, 1f)
            button3FadeInAnimator.duration = 1000

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(stageTextFadeInAnimator, obstacleTextFadeInAnimator, button1FadeInAnimator, button2FadeInAnimator, button3FadeInAnimator)
            animatorSet.start()
        }
    }

    fun showSceneContent (scene:Scene, isAnimated: Boolean) {
        val mainText = resources.getString(scene.sceneTextId)
        val obstacleList = mutableListOf<String>()
        scene.obstacles.forEach {
            val obstacleAsText = resources.getString(it.textId, it.totalValue)
            obstacleList.add(obstacleAsText)
        }
        val optionList = mutableListOf<String>()
        scene.options.forEach {
            optionList.add(resources.getString(it.textId))
        }
        val isOptionEnabled = viewModel.checkObstaclesResolved(getCurrentDate())

        showSceneContent(mainText, obstacleList, optionList, isOptionEnabled, isAnimated)
    }

    private fun setButtonState (button: Button, text: String?, isEnabled: Boolean) {
        if (text==null) button.visibility = View.GONE
        else {
            button.visibility = View.VISIBLE
            button.isEnabled = isEnabled
            button.text = text
        }
    }

    private fun showLoading () {
        scenePicture.visibility = View.INVISIBLE
        hidingImageView.visibility = View.INVISIBLE
        titleTextView.visibility = View.INVISIBLE
        contentLayout.visibility = View.INVISIBLE

        progress_bar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideLoading () {
        progress_bar.visibility = View.INVISIBLE

        scenePicture.visibility = View.VISIBLE
        hidingImageView.visibility = View.VISIBLE
        titleTextView.visibility = View.VISIBLE
        contentLayout.visibility = View.VISIBLE

        isLoading = false
    }

    private fun stopSound() {
        mediaPlayer?.stop()
    }
}
