package com.example.betssonrequest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.core.Client
import com.example.core.EndPointService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

/**
 * https://gist.github.com/RohitSurwase/6a31ecdbcdc7e8712a6b517e305d7308
 */

class TestService : Service(), CoroutineScope {

    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext get() = mJob + Dispatchers.Main

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runBlocking {
            runCatching {
                Client().retrofit()
                    .create(EndPointService::class.java)
                    .noPassword("9906acf0-e863-4a03-b7ff-225ec4f624fb")
            }.onSuccess {
                Log.d("OkHttp", "Success: ${it.body()}")
            }.onFailure {
                Log.d("OkHttp", "Failed:  ${it.message}")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onCreate() {
        super.onCreate()
        while (true) {
            Log.d("TAG", "TestService")
            Thread.sleep(1000)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mJob.cancel()
    }
}