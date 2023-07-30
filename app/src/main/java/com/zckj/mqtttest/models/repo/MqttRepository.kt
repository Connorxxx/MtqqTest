package com.zckj.mqtttest.models.repo

import android.os.Build
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.utils.logCat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class MqttRepository @Inject constructor() {

    private fun clientId() = "Android_${Build.MODEL}_${Build.DEVICE}_${(0..250).random()}"

    suspend fun connectMqtt(
        serverUri: String,
        user: String,
        pass: ByteArray,
        clientId: String = clientId(),
    ) = withContext(Dispatchers.IO) {
            runCatching {
                val client = MqttClient(serverUri, clientId, MemoryPersistence())
                val options = MqttConnectionOptions().apply {
                    userName = user
                    password = pass
                    keepAliveInterval = 20
                    connectionTimeout = 15
                    isSendReasonMessages = true
                }
                client.apply { connect(options) }
            }
        }

    fun getState(client: MqttClient?) = callbackFlow {
        client?.setCallback(object : MqttCallback {
            override fun disconnected(p0: MqttDisconnectResponse?) {
                p0?.let { trySend(Mqtt.Lost(it)) }
            }

            override fun mqttErrorOccurred(p0: MqttException?) {
                p0?.let { trySend(Mqtt.Error(it)) }
            }

            override fun messageArrived(p0: String?, p1: MqttMessage?) {
                p1?.let {
                    trySend(Mqtt.Received(p0 ?: "", it))
                }
            }

            override fun deliveryComplete(p0: IMqttToken?) {
            }

            override fun connectComplete(p0: Boolean, p1: String?) {
            }

            override fun authPacketArrived(p0: Int, p1: MqttProperties?) {
            }

        })
        awaitClose()
    }.flowOn(Dispatchers.IO)

}