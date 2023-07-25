package com.zckj.mqtttest.utils

import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.common.MqttMessage

fun MqttClient.disConnect() = runCatching { disconnect() }

fun MqttClient.subscribeTopic(topic: String, q: Int = 1) = runCatching { subscribe(topic, q) }

fun MqttClient.unsubscribeTopic(topic: String) = runCatching { unsubscribe(topic) }

fun MqttClient.publishMessage(topic: String, msg: String, q: Int = 1) = runCatching {
    MqttMessage(msg.toByteArray()).apply {
        qos = q
        publish(topic, this)
    }
}



