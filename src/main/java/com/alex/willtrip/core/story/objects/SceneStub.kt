package com.alex.willtrip.core.story.objects

import com.alex.willtrip.objectbox.converters.ListOfIntConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
class SceneStub (@Id var id: Long = 0, @Index val link: Int = 0, val themeLink: Int = 0, val mainTextId: Int = 0,
                 @Convert(converter = ListOfIntConverter::class, dbType = String::class)
                 val optionLinkArray: ArrayList <Int> = arrayListOf(),
                 @Convert(converter = ListOfIntConverter::class, dbType = String::class)
                 val obstacleLinkArray: ArrayList <Int> = arrayListOf())