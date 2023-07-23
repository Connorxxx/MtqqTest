package com.zckj.mqtttest.viewmodels

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
    private val clientId = "android_001"

    private val _mqttEvent = MutableSharedFlow<Mqtt>()
    val mqttEvent = _mqttEvent.asSharedFlow()

    var client: MqttClient? = null
        private set

    fun senMqtt(mqtt: Mqtt) {
        viewModelScope.launch {
            _mqttEvent.emit(mqtt)
        }
    }

    fun connect(serverUri: String) {
        viewModelScope.launch() {
            runCatching {
                withContext(Dispatchers.IO) {
                    mqttRepo.connectMqtt(serverUri, clientId)
                }
            }.onSuccess {
                client = it
            }.onFailure {
                "Error: ${it.localizedMessage}".showToast()
            }
            client?.setCallback(object : MqttCallback {
                override fun disconnected(p0: MqttDisconnectResponse?) {

                }

                override fun mqttErrorOccurred(p0: MqttException?) {
                    p0?.logCat()
                }

                override fun messageArrived(topic: String?, msg: MqttMessage?) {
                    "$topic -> $msg".logCat()
                    senMqtt(Mqtt.Received(topic ?: "", msg ?: MqttMessage()))
                }

                override fun deliveryComplete(p0: IMqttToken?) {

                }

                override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                    "connectComplete: $reconnect -> $serverURI".logCat()
                }

                override fun authPacketArrived(p0: Int, p1: MqttProperties?) {

                }

            })
        }
    }

    fun publishMessage(topic: String, msg: String) {
        MqttMessage(msg.toByteArray()).apply {
            qos = 1
            client?.publish(topic, this)
        }
    }
}