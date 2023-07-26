package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zckj.mqtttest.utils.subscribeTopic
import com.zckj.mqtttest.utils.unsubscribeTopic
import com.zckj.mqtttest.viewmodels.MainViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun Another(vm: MainViewModel = hiltViewModel()) {
    //var topic by rememberSaveable { mutableStateOf("") }
    // var connect by rememberSaveable { mutableStateOf("tcp://") }
    Scaffold(
        bottomBar = { BottomBar(vm, vm.topic) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(bottom = 24.dp)) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
            Column(Modifier.padding(start = 8.dp, end = 8.dp)) {
                TextField(
                    value = vm.connect,
                    onValueChange = { vm.connect = it },
                    label = { Text(vm.connectState) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { vm.connect(vm.connect) }
                    ) {
                        Text(text = "Connect", maxLines = 1)
                    }
                    Button(
                        onClick = {
                            vm.disConnect()
                        }
                    ) {
                        Text(text = "Disconnect", maxLines = 1)
                    }
                }
                TextField(
                    value = vm.topic,
                    onValueChange = { vm.topic = it },
                    label = { Text("Topic") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { vm.client?.subscribeTopic(vm.topic) }
                    ) {
                        Text(text = "Subscribe", maxLines = 1)
                    }
                    Button(
                        onClick = { vm.client?.unsubscribeTopic(vm.topic) }
                    ) {
                        Text(text = "Unsubscribe", maxLines = 1)
                    }
                }
                ReceiveList(messages = vm.itemsList)
            }
        }
    }
}

@Composable
fun ReceiveList(messages: List<String>) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState
    ) {
        items(items = messages) { msg ->
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = msg,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }

    LaunchedEffect(messages) {
        snapshotFlow { messages.size }
            .filter { messages.isNotEmpty() }
            .collect { size ->
                lazyListState.scrollToItem(size - 1)
            }
    }
}

@Composable
private fun BottomBar(vm: MainViewModel, topic: String) {
    Row(Modifier.padding(start = 12.dp, end = 12.dp)) {
        TextField(
            value = vm.msg,
            onValueChange = { vm.msg = it },
            label = { Text(text = "Message") },
            modifier = Modifier.weight(3f)
        )
        Button(
            onClick = { vm.publishMessage(topic, vm.msg) },
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
                .weight(1.2f)
        ) {
            Text(text = "Send", maxLines = 1)
        }
    }
}