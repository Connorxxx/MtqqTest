package com.zckj.mqtttest.event

import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.common.MqttMessage

sealed interface Mqtt {
    data class Received(val topic: String, val message: MqttMessage) : Mqtt
    data class Complete(val reconnect: Boolean, val serverURI: String) : Mqtt
    data class Lost(val cause: MqttDisconnectResponse) : Mqtt
}