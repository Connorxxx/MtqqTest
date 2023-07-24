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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zckj.mqtttest.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Received(vm: MainViewModel) {
    var topic by rememberSaveable { mutableStateOf("") }
    var connect by rememberSaveable { mutableStateOf("tcp://") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = connect,
            onValueChange = { connect = it },
            label = { Text(vm.connectState) },
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
                onClick = { vm.connect(connect) }
            ) {
                Text(text = "Connect", maxLines = 1)
            }
            Button(
                onClick = { vm.client?.disconnect() }
            ) {
                Text(text = "Disconnect", maxLines = 1)
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(24.dp))
        TextField(
            value = topic,
            onValueChange = { topic = it },
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
                onClick = { vm.client?.subscribe(topic, 1) }
            ) {
                Text(text = "Subscribe", maxLines = 1)
            }
            Button(
                onClick = { vm.client?.unsubscribe(topic) }
            ) {
                Text(text = "Unsubscribe", maxLines = 1)
            }
        }
    }
}