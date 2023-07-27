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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.zckj.mqtttest.viewmodels.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextSwitcher(vm: MainViewModel = hiltViewModel()) {
    val seconds by vm.seconds.collectAsState(initial = "00")

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = seconds,
            transitionSpec = { addAnimation().using(SizeTransform(false)) },
            label = ""
        ) {
            Text(
                text = "$it",
                style = TextStyle(fontSize = MaterialTheme.typography.displayLarge.fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun addAnimation(duration: Int = 500): ContentTransform {
    return slideInVertically(tween(duration)) {
        it
    } + fadeIn(tween(duration)) with slideOutVertically(tween(duration)) {
        -it
    } + fadeOut(tween(duration))
}