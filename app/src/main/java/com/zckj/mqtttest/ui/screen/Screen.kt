package com.zckj.mqtttest.ui.screen

import androidx.annotation.StringRes
import com.zckj.mqtttest.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    data object Home : Screen("home", R.string.home)
    data object Setting : Screen("setting",R.string.setting)
    data object Message : Screen("message", R.string.message)
    data object Another1 : Screen("another1", R.string.another_1)
    data object Another2 : Screen("another2", R.string.another_2)
    data object Tabs : Screen("tabs", R.string.tab)
}
