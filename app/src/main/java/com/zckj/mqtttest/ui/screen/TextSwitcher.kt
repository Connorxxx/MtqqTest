package com.zckj.mqtttest.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zckj.mqtttest.viewmodels.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextSwitcher(vm: MainViewModel = hiltViewModel()) {
    //val seconds by vm.seconds.collectAsState(initial = "00")
    var seconds by remember { mutableStateOf(0) }
    var previousSecond by remember { mutableStateOf(0) }

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
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
                    addAnimation()
                } else {
                    addAnimation(false)
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
}

@OptIn(ExperimentalAnimationApi::class)
fun addAnimation(isUp: Boolean = true) =
    slideInVertically { if (isUp) it else -it } + fadeIn() with
            slideOutVertically { if (isUp) -it else it } + fadeOut()
