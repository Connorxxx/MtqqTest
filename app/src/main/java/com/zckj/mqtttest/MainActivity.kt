package com.zckj.mqtttest

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zckj.mqtttest.ui.screen.Home
import com.zckj.mqtttest.ui.screen.Screen
import com.zckj.mqtttest.ui.screen.Test
import com.zckj.mqtttest.ui.theme.MqttTestTheme
import com.zckj.mqtttest.utils.Event
import com.zckj.mqtttest.utils.Route
import com.zckj.mqtttest.utils.logCat
import com.zckj.mqtttest.utils.navigateSingleTopTo
import com.zckj.mqtttest.utils.subscribe
import com.zckj.mqtttest.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

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
    val navigateToScreen: (String) -> Unit = { navController.navigateSingleTopTo(it) }
    val viewModel: MainViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        subscribe<Route> {
            navController.navigate(it.route)
        }
    }
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            Home()
        }
        composable(Screen.Test.route) {
            Test(viewModel)
        }
    }
}