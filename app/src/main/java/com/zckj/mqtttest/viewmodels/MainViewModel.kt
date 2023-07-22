package com.zckj.mqtttest.viewmodels

import androidx.lifecycle.ViewModel
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.models.repo.MqttRepository
import com.zckj.mqtttest.utils.logCat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    mqttRepo: MqttRepository
) : ViewModel() {

    private val serverUri = "tcp://192.168.3.46:1883"
    private val clientId = "android_001"

    private val _mqttEvent = MutableSharedFlow<Mqtt>()
    val mqttEvent = _mqttEvent.asSharedFlow()

    private val mqtt = mqttRepo.connectMqtt(serverUri, clientId)

    fun subscribe(topic: String, qos: Int) {
        mqtt?.subscribe(topic, qos)
    }

    init {
        mqtt?.setCallback(object : MqttCallback {
            override fun disconnected(p0: MqttDisconnectResponse?) {

            }

            override fun mqttErrorOccurred(p0: MqttException?) {

            }

            override fun messageArrived(topic: String?, msg: MqttMessage?) {
                "$topic -> $msg".logCat()
            }

            override fun deliveryComplete(p0: IMqttToken?) {

            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                "$reconnect -> $serverURI".logCat()
            }

            override fun authPacketArrived(p0: Int, p1: MqttProperties?) {

            }

        })
    }
}