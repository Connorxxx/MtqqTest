package com.zckj.mqtttest.models.repo

import com.zckj.mqtttest.utils.logCat
import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MqttRepository @Inject constructor() {

    fun connectMqtt(serverUri: String, clientId: String) = runCatching {
        val client = MqttClient(serverUri, clientId, MemoryPersistence())
        val options = MqttConnectionOptions()
        client.connect(options)
        if (client.isConnected) "Connected to MQTT server".logCat()
        else "Failed to connect to MQTT server".logCat()
        client
    }.getOrNull()

}