package com.alex.willtrip.willpower

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class WillPower {
    @Id var id: Long = 0
    var willPower: Int = 0

    set(value) {
        field = if (value >= 0) value
        else 0
    }
}