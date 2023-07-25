package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zckj.mqtttest.utils.navigateSingleTopTo
import com.zckj.mqtttest.viewmodels.MainViewModel

@Composable
fun Home() {
    val navController = rememberNavController()
    val item = listOf(Screen.Setting, Screen.Message)
    val viewModel: MainViewModel = hiltViewModel()
    Scaffold(
        topBar = { HomeTopBar() },
        bottomBar = { BottomBar(navController = navController, item = item) },
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Setting.route,
            Modifier.padding(it)
        ) {
            composable(Screen.Setting.route) {
                Setting(viewModel)
            }
            composable(Screen.Message.route) {
                Message(viewModel)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Home", fontWeight = FontWeight.W400) },
    )
}

@Composable
fun BottomBar(navController: NavHostController, item: List<Screen>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        item.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route)
                }
            )
        }
    }
}