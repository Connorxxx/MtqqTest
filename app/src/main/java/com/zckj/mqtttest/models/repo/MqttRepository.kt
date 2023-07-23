package com.zckj.mqtttest.models.repo

import com.zckj.mqtttest.utils.logCat
import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class MqttRepository @Inject constructor() {

    suspend fun connectMqtt(serverUri: String, clientId: String) = suspendCoroutine {
        val client = MqttClient(serverUri, clientId, MemoryPersistence())
        val options = MqttConnectionOptions()
        client.connect(options)
        if (client.isConnected) {
            "Connected to MQTT server".logCat()
            it.resumeWith(Result.success(client))
        }
        else {
            "Failed to connect to MQTT server".logCat()
            it.resumeWith(Result.failure(Exception("Failed to connect to MQTT server")))
        }
    }//.getOrNull()

}