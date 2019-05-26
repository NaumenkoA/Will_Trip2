package com.alex.willtrip.core.story.objects

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class Theme (@Id var id: Long = 0, @Index val link: Int, val drawableId: Int, val titleTextId: Int, val titleTintColorId: Int, val soundId: Int)