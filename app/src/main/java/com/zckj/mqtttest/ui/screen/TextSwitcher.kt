package com.zckj.mqtttest.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zckj.mqtttest.utils.Route
import com.zckj.mqtttest.utils.post
import com.zckj.mqtttest.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextSwitcher(vm: MainViewModel = hiltViewModel()) {
    //val seconds by vm.seconds.collectAsState(initial = "00")
    val bottomState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { scope.post(Route(Screen.SwipeToDismiss)) }) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Localized description",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                bottomState.partialExpand()
                            }
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                }
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            BottomSheet(bottomState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(bottomState: SheetState) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(bottomState)
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 384.dp,
        sheetContent = {
            Sheet(scope, scaffoldState)
        }
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimationNumber()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Sheet(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    var sliderPosition by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text("Swipe up to expand sheet")
    }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
          //.padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sheet content")
        Spacer(Modifier.height(20.dp))
        FilledTonalButton(
            onClick = {
                scope.launch { scaffoldState.bottomSheetState.hide() }
            }
        ) {
            Text("Click to hide sheet")
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Slider(
                modifier = Modifier
                    .semantics { contentDescription = "Localized Description" }
                    .weight(1f),
                value = sliderPosition,
                valueRange = 0f..100f,
                onValueChange = { sliderPosition = it }
            )
            Text(text = sliderPosition.toInt().toString(), modifier = Modifier
                //.weight(1f)
                .padding(start = 12.dp), maxLines = 1)
        }

    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun AnimationNumber() {
    var seconds by remember { mutableStateOf(0) }
    var previousSecond by remember { mutableStateOf(0) }
    Button(
        onClick = {
            previousSecond = seconds
            seconds++
        }) {
        Text(text = "Up")
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    )
    AnimatedContent(
        targetState = seconds,
        transitionSpec = {
            if (seconds > previousSecond) {
                contentTransform(AnimatedContentScope.SlideDirection.Up)
            } else {
                contentTransform(AnimatedContentScope.SlideDirection.Down)
            }.using(SizeTransform(false))
        },
        label = ""
    ) {
        Text(
            text = if (it in 0..9) "0$it" else "$it",
            style = TextStyle(fontSize = MaterialTheme.typography.displayLarge.fontSize),
            textAlign = TextAlign.Center
        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    )
    Button(onClick = {
        previousSecond = seconds
        seconds--
    }) {
        Text(text = "Down")
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<Int>.contentTransform(towards: AnimatedContentScope.SlideDirection) =
    slideIntoContainer(towards) + fadeIn() with
            slideOutOfContainer(towards) + fadeOut()

@OptIn(ExperimentalAnimationApi::class)
fun addAnimation(isUp: Boolean = true) =
    slideInVertically { if (isUp) it else -it } + fadeIn() with
            slideOutVertically { if (isUp) -it else it } + fadeOut()
