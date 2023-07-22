package com.zckj.mqtttest.ui.screen

import androidx.annotation.StringRes
import com.zckj.mqtttest.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Received : Screen("received",R.string.received)
    object Send : Screen("send", R.string.send)
}
