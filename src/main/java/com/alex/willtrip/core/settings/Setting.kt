package com.alex.willtrip.core.settings

enum class Setting (val default: Int,val maxValue: Int) {
    WEEK_STARTS_MON(1,1),
    CHAIN_POINTS(1 ,1),
    SPECIAL_DAYS(1,1),
    DELAYED_DAYS(3,3),
    GRATITUDE (1, 1),
    NOTIFICATIONS(1, 1),
    NOTIFICATION_TIME(79200, 86399),
    EVERY_N_DAYS_STRICT(0, 1)
}