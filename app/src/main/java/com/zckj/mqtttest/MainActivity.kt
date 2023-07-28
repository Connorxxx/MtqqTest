package com.zckj.mqtttest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zckj.mqtttest.ui.screen.Home
import com.zckj.mqtttest.ui.screen.Screen
import com.zckj.mqtttest.ui.screen.SwipeDismiss
import com.zckj.mqtttest.ui.screen.TabTest
import com.zckj.mqtttest.ui.screen.TextSwitcher
import com.zckj.mqtttest.ui.theme.MqttTestTheme
import com.zckj.mqtttest.utils.Route
import com.zckj.mqtttest.utils.subscribe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MqttTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHome()
                }
            }
        }
    }
}

@Composable
fun NavHome() {
    val navController = rememberNavController()
    LaunchedEffect(Unit) {
        subscribe<Route> {
            navController.navigate(it.screen.route)
        }
    }
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            Home()
        }
        composable(Screen.Tabs.route) {
            TabTest()
        }
        composable(Screen.TextSwitcher.route) {
            TextSwitcher()
        }
        composable(Screen.SwipeToDismiss.route) {
            SwipeDismiss()
        }
    }
}