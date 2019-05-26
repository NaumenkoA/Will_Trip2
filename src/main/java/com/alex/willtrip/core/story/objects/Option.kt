package com.alex.willtrip.core.story.objects

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class Option (@Id var id: Long = 0, @Index val link: Int, val textId: Int, val nextSceneLink: Int?)
