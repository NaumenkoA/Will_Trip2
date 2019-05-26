package com.alex.willtrip.core.story.interfaces

import com.alex.willtrip.core.story.objects.Story

interface StoryLoader {
    fun loadStory (story: Story)
}