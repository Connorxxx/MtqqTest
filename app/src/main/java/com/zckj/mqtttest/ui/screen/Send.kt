package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.utils.logCat
import com.zckj.mqtttest.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Send(vm: MainViewModel) {
    var receiveState by rememberSaveable { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    val mqttEventState by vm.mqttEvent.collectAsState("")
    if (mqttEventState is Mqtt.Received) {
        receiveState = (mqttEventState as Mqtt.Received).message.toString()
        "rec: $receiveState".logCat()
    }
    Column {
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Topic") },
                modifier = Modifier
            )
            Button(
                onClick = { vm.publishMessage("test", text) },
                Modifier.padding(start = 8.dp)
            ) {
                Text(text = "Send")
            }
        }

    Text(
        text = receiveState,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        modifier = Modifier.padding(start = 12.dp)
    )
    }

}