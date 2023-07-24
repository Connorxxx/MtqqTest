package com.zckj.mqtttest.viewmodels

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.models.repo.MqttRepository
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
    private val mqttRepo: MqttRepository
) : ViewModel() {

//    private val serverUri = "tcp://192.168.0.112:1883"
    private val clientId = "Android_${Build.MODEL}_${Build.DEVICE}"

    var receiveState by mutableStateOf("")
        private set

    var connectState by mutableStateOf("Connect")
        private set

    var client: MqttClient? = null
        private set

    fun connect(serverUri: String) {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    mqttRepo.connectMqtt(serverUri, clientId)
                }
            }.onSuccess {
                client = it
                connectState = "Connected"
            }.onFailure {
                connectState = "Error"
                "Error: ${it.localizedMessage}".showToast()
            }
            "server: ${client?.serverURI} client: ${client?.clientId}".logCat()
            mqttRepo.getState(client).collect {
                when (it) {
                    is Mqtt.Received -> receiveState = "Topic: ${it.topic}\n\n ${it.message}"
                    is Mqtt.Lost -> "Mqtt Lost: ${it.cause.reasonString} ${it.cause.returnCode}".logCat()
                    is Mqtt.Error -> "Mqtt Error: ${it.e.localizedMessage}".logCat()
                }
            }
        }
    }

    fun publishMessage(topic: String, msg: String) {
        MqttMessage(msg.toByteArray()).apply {
            qos = 1
            client?.publish(topic, this)
        }
    }
}