package com.zckj.mqtttest.ui.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.zckj.mqtttest.viewmodels.MainViewModel

@Composable
fun Test(vm: MainViewModel = hiltViewModel()) {

    Button(onClick = {
        vm.connect("tcp://192.168.3.46:1883")
    }) {
        Text(text ="Connect")
    }
}