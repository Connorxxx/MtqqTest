package com.zckj.mqtttest.viewmodels

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.models.repo.MqttRepository
import com.zckj.mqtttest.usecase.ConnectUseCase
import com.zckj.mqtttest.utils.logCat
import com.zckj.mqtttest.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectUseCase: ConnectUseCase,
) : ViewModel() {

    private val clientId = "Android_${Build.MODEL}_${Build.DEVICE}_${(0..250).random()}"

    var receiveState by mutableStateOf("")
        private set

    var connectState by mutableStateOf("Not connect yet")
        private set

    private var client: MqttClient? = null

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
        runCatching { client?.disconnect() }.onSuccess { connectState = "Disconnect" }
    }

    fun subscribe(topic: String) {
        runCatching { client?.subscribe(topic, 1) }
    }

    fun unsubscribe(topic: String) {
        runCatching { client?.unsubscribe(topic) }
    }

    fun publishMessage(topic: String, msg: String) {
        MqttMessage(msg.toByteArray()).apply {
            qos = 1
            runCatching { client?.publish(topic, this) }.onFailure {
                "Error: ${it.localizedMessage}".showToast()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        "Main ViewModel -> onCleared".logCat()
    }
}