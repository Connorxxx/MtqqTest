package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zckj.mqtttest.utils.logCat
import com.zckj.mqtttest.viewmodels.MainViewModel

@Composable
fun Test(vm: MainViewModel = hiltViewModel()) {
    var connect by rememberSaveable { mutableStateOf("tcp://") }
    var topic by rememberSaveable { mutableStateOf("") }
    Scaffold(
        bottomBar = { BottomBar(vm, topic) }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
            Column(Modifier.padding(start = 8.dp, end = 8.dp)) {
                TextField(
                    value = connect,
                    onValueChange = { connect = it },
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
                        onClick = { vm.connect(connect) }
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
                    value = topic,
                    onValueChange = { topic = it },
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
                        onClick = { vm.subscribe(topic) }
                    ) {
                        Text(text = "Subscribe", maxLines = 1)
                    }
                    Button(
                        onClick = { vm.unsubscribe(topic) }
                    ) {
                        Text(text = "Unsubscribe", maxLines = 1)
                    }
                }
                Text(
                    text = vm.receiveState,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomBar(vm: MainViewModel, topic: String) {
    var text by remember { mutableStateOf("") }
    Row(Modifier.padding(start = 12.dp, end = 12.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(text = "Message") },
            modifier = Modifier.weight(3f)
        )
        Button(
            onClick = { vm.publishMessage(topic, text) },
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically)
                .weight(1.2f)
        ) {
            Text(text = "Send", maxLines = 1)
        }
    }
}