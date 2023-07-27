package com.zckj.mqtttest.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay


class ConnectWork(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val data = inputData.getString("URL_DATA") ?: return Result.failure()
        (0..100).forEach {
            setProgress(workDataOf("Progress" to it))
            delay(20)
        }
        val result = Data.Builder()
            .putString("URL_DATA", data)
            .build()
        return Result.success(result)
    }
}