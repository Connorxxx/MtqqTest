package com.zckj.mqtttest.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.usecase.ConnectUseCase
import com.zckj.mqtttest.utils.disConnect
import com.zckj.mqtttest.utils.logCat
import com.zckj.mqtttest.utils.publishMessage
import com.zckj.mqtttest.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.eclipse.paho.mqttv5.client.MqttClient
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectUseCase: ConnectUseCase,
) : ViewModel() {

    init {
        "ViewModel Init".logCat()
    }

    var connect by mutableStateOf("tcp://")
    var topic by mutableStateOf("")
    var msg by mutableStateOf("")
    val itemsList = mutableStateListOf<String>()

    var receiveState by mutableStateOf("")
        private set

    var connectState by mutableStateOf("Not connect yet")
        private set

    var client: MqttClient? = null
        private set

    fun connect(serverUri: String, user: String = "", pass: ByteArray = "".toByteArray()) {
        viewModelScope.launch {
            connectUseCase(serverUri, user, pass).onSuccess {
                client = it
                connectState = if (it.isConnected) "Connected" else "Connected failed"
                "Mqtt connect: ${it.isConnected} ${it.clientId}".logCat()
            }.onFailure {
                    connectState = "Error"
                    "Error: ${it.localizedMessage}".showToast()
                }
            connectUseCase(client) {
                when (it) {
                    is Mqtt.Received -> {
                        itemsList.add("Topic: ${it.topic}\n\n ${it.message}")
                        receiveState = "Topic: ${it.topic}\n\n ${it.message}"
                        receiveState.logCat()
                    }
                    is Mqtt.Lost -> {
                        "Mqtt Lost: ${it.cause}".logCat()
                        if (it.cause.returnCode != 142) connectState = "Lost"
                    }
                    is Mqtt.Error -> "Mqtt Error: ${it.e.localizedMessage}".logCat()
                }
            }
        }
    }

    fun disConnect() {
        client?.let {
            it.disConnect().onSuccess { connectState = "Disconnect" }
        }
    }

    fun publishMessage(topic: String, msg: String) {
        client?.let {
            it.publishMessage(topic, msg).onFailure { error ->
                "Error: ${error.localizedMessage}".showToast()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disConnect()
        "Main ViewModel -> onCleared".logCat()
    }
}