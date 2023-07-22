package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Received(vm: MainViewModel) {
    var text by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Topic") },
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp)
        )
        Row(Modifier.padding(top = 12.dp)) {
            Button(
                onClick = { vm.client?.subscribe(text, 1) },
                Modifier.padding(start = 8.dp, end = 8.dp).weight(1f)
            ) {
                Text(text = "Subscribe")
            }
            Button(
                onClick = { vm.client?.subscribe(text, 1) },
                Modifier.padding(start = 8.dp, end = 8.dp).weight(1f)
            ) {
                Text(text = "Unsubscribe")
            }
        }
    }
}