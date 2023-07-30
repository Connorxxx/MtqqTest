package com.zckj.mqtttest.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.unit.dp
import com.zckj.mqtttest.utils.logCat
import kotlinx.coroutines.delay

@Composable
fun DismissList(){
    val msgList = remember { mutableStateListOf<String>() }
    (0..50).forEach {
        msgList.add(it.toString())
    }
    LazyColumn {
        items(msgList, { it }) {
            SwipeDismiss(it, msgList)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeDismiss(idx: String, msgList: SnapshotStateList<String>) {
    val dismissState = rememberDismissState()
    var cardVisible by remember { mutableStateOf(true) }
    var show by remember { mutableStateOf(Show.Default) }
    when (dismissState.currentValue) {
        DismissValue.DismissedToStart, DismissValue.DismissedToEnd -> msgList.remove(idx)
        else -> {}
    }
    SwipeToDismiss(
        modifier = Modifier.padding(top = 24.dp),
        state = dismissState,
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.primary
                    DismissValue.DismissedToStart -> MaterialTheme.colorScheme.error
                }, label = ""
            )
            cardVisible = when (dismissState.currentValue) {
                DismissValue.Default -> true
                DismissValue.DismissedToEnd -> false
                DismissValue.DismissedToStart -> false
            }
            show = when (dismissState.targetValue) {
                DismissValue.Default -> Show.Default
                DismissValue.DismissedToEnd -> Show.CHECK
                DismissValue.DismissedToStart -> Show.Delete
            }
            AnimatedVisibility(
                visible = cardVisible,
                exit = fadeOut(tween(300))
            ) {
                Card(
                    Modifier
                        .fillMaxSize(),
                    colors = CardDefaults.cardColors(color)
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(visible = show == Show.CHECK) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = "CheckCircle",
                                Modifier
                                    .size(44.dp)
                                    .padding(start = 12.dp)
                            )
                        }
                        Box {}
                        AnimatedVisibility(
                            visible = show == Show.Delete,
                            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
                            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start)
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                Modifier
                                    .size(44.dp)
                                    .padding(end = 12.dp)
                            )
                        }
                    }
                }
            }
        },
        dismissContent = {
            Card {
                ListItem(
                    headlineContent = {
                        Text("$idx -> Cupcake", Modifier.clickable {
                            msgList.remove(idx)
                        })
                    },
                    supportingContent = { Text("Swipe me left or right!") }
                )
            }
        }
    )
}

enum class Show {
    Default, Delete, CHECK
}
