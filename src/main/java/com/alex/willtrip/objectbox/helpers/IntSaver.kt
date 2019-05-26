package com.alex.willtrip.objectbox.helpers

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
class IntSaver (@Id var id: Long = 0, @Index val link: Int, val value: Int)