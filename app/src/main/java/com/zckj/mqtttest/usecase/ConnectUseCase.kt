package com.zckj.mqtttest.usecase

import android.os.Build
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.models.repo.MqttRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.paho.mqttv5.client.MqttClient
import javax.inject.Inject

@ViewModelScoped
class ConnectUseCase @Inject constructor(
    private val mqttRepo: MqttRepository
) {

    private val clientId = "Android_${Build.MODEL}_${Build.DEVICE}_${(0..250).random()}"

    suspend operator fun invoke(
        serverUri: String,
        user: String = "",
        pass: ByteArray = "".toByteArray()
    ) = withContext(Dispatchers.IO) {
        mqttRepo.connectMqtt(serverUri, user, pass, clientId)
    }

    suspend operator fun invoke(client: MqttClient?, block: (Mqtt) -> Unit) =
        withContext(Dispatchers.IO) {
            mqttRepo.getState(client).collect {
                block(it)
            }
        }
}