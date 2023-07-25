package com.zckj.mqtttest.utils

import android.util.Log
import android.widget.Toast
import androidx.navigation.NavHostController
import com.zckj.mqtttest.App
import com.zckj.mqtttest.ui.screen.Screen


fun Any.logCat(tab: String = "MQTT_TEST_LOG") {
  //  if (!BuildConfig.DEBUG) return
    if (this is String) Log.d(tab, this) else Log.d(tab, this.toString())
}

fun String.showToast() {
    Toast.makeText(App.app, this, Toast.LENGTH_SHORT).show()
}

fun NavHostController.navigateSaveState(route: String) =
    this.navigate(route) {
        popUpTo(Screen.Home.route) {
            saveState = true
            inclusive = false
        }
        restoreState = true
    }