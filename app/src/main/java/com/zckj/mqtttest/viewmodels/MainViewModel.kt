package com.zckj.mqtttest.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.zckj.mqtttest.event.Mqtt
import com.zckj.mqtttest.usecase.ConnectUseCase
import com.zckj.mqtttest.utils.disConnect
import com.zckj.mqtttest.utils.logCat
import com.zckj.mqtttest.utils.publishMessage
import com.zckj.mqtttest.utils.showToast
import com.zckj.mqtttest.work.ConnectWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.eclipse.paho.mqttv5.client.MqttClient
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectUseCase: ConnectUseCase,
    private val workManager: WorkManager,
) : ViewModel() {

    // private val workManager = WorkManager.getInstance(application)

    init {
        "ViewModel Init".logCat()
    }

    var connect by mutableStateOf("tcp://")
    var topic by mutableStateOf("")
    var msg by mutableStateOf("")
    val itemsList = mutableStateListOf<String>()

    var receiveState by mutableStateOf("")
        private set

    var connectState by mutableStateOf("Not connect yet")
        private set

    var client: MqttClient? = null
        private set

    fun testWork() {
        val testWork = OneTimeWorkRequestBuilder<ConnectWork>()
                .setInputData(workDataOf("URL_DATA" to connect))
                .build()

        workManager.enqueueUniqueWork(testWork.id.toString(), ExistingWorkPolicy.KEEP, testWork)
        workManager.getWorkInfoByIdLiveData(testWork.id).asFlow().onEach {
            it?.let {
                it.progress.getInt("Progress", 0).logCat()
                if (it.state.isFinished) it.outputData.getString("URL_DATA")?.logCat()
            }
        }.launchIn(viewModelScope)
    }

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
                        itemsList.add("Topic: ${it.topic}\n\n ${it.message}")
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
        client?.let {
            it.disConnect().onSuccess { connectState = "Disconnect" }
        }
    }

    fun publishMessage(topic: String, msg: String) {
        client?.let {
            it.publishMessage(topic, msg).onFailure { error ->
                "Error: ${error.localizedMessage}".showToast()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disConnect()
        "Main ViewModel -> onCleared".logCat()
    }
}