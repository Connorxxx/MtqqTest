package com.zckj.mqtttest.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zckj.mqtttest.utils.Route
import com.zckj.mqtttest.utils.post
import com.zckj.mqtttest.utils.subscribeTopic
import com.zckj.mqtttest.utils.unsubscribeTopic
import com.zckj.mqtttest.viewmodels.MainViewModel

@Composable
fun Setting(vm: MainViewModel) {
   // var topic by rememberSaveable { mutableStateOf("") }
    var user by rememberSaveable { mutableStateOf("") }
    var passwd by rememberSaveable { mutableStateOf("") }
   // var connect by rememberSaveable { mutableStateOf("tcp://") }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = vm.connect,
            onValueChange = { vm.connect = it },
            label = { Text(vm.connectState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                label = { Text("User") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            )
            OutlinedTextField(
                value = passwd,
                onValueChange = { passwd = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                    .weight(1f)
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { vm.connect(vm.connect, user, passwd.toByteArray()) }
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
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )
        TextField(
            value = vm.topic,
            onValueChange = { vm.topic = it },
            label = { Text("Topic") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
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
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { scope.post(Route(Screen.Tabs)) },
            ) {
                Text(text = "Tabs")
            }
            Button(onClick = { vm.testWork() }) {
                Text(text = "test")
            }
            Button(onClick = { scope.post(Route(Screen.TextSwitcher)) } ) {
                Text(text = "TextSwitcher")
            }
        }

    }
}