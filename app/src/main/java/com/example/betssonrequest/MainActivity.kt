package com.example.betssonrequest

/**
 * https://proandroiddev.com/coroutines-for-android-6f9b9f966056
 *
 * https://developer.android.com/kotlin/coroutines/coroutines-adv
 *
 * https://developer.android.com/kotlin/coroutines?gclid=CjwKCAjw9r-DBhBxEiwA9qYUpfhME9c2-I43aO_nC5n6ksReC0MZCtnihXqyem5NyLMJf_8QR49fYxoCkUgQAvD_BwE&gclsrc=aw.ds
 *
 * https://developer.android.com/kotlin/coroutines/coroutines-adv
 *
 * https://www.youtube.com/watch?v=_hfBv0a09Jc
 *
 * https://github.com/Kotlin/KEEP/blob/master/proposals/coroutines.md
 */

/**
 * https://developer.android.com/guide/components/services
 */

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.content.Intent
import android.util.Log
import com.example.core.Client
import com.example.core.service.EndPointService
import com.example.core.service.TwitterService
import com.example.twitter.ui.TwitterLogInBottomSheet

class MainActivity : AppCompatActivity(), CoroutineScope {
    private var states = arrayOf(
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(android.R.attr.state_focused),
        intArrayOf(android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_checkable),
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf(android.R.attr.state_window_focused)
    )

    private var colors = intArrayOf(
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN
    )

    var myList = ColorStateList(states, colors)

    private lateinit var mJob: Job

    override val coroutineContext: CoroutineContext get() = mJob + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mJob = Job()
        setContentView(R.layout.activity_main)
        val txtErrorInputLayout: TextInputLayout = findViewById(R.id.txtErrorInputLayout)
        val txtErrorInput: TextInputEditText = findViewById(R.id.txtErrorInput)

        val test: TextInputEditText = findViewById(R.id.test_box)
        val testBoxLayout: MyTextInputLayout = findViewById(R.id.test_box_layout)

        val txbShare: TextInputEditText = findViewById(R.id.txb_share)
        val btnShare: MaterialButton = findViewById(R.id.btn_share)
        val tilShare: MyTextInputLayout = findViewById(R.id.til_share)

        btnShare.setOnClickListener {
            if(txbShare.text.isNullOrBlank()) {
                tilShare.error = "Please fill the box"
            } else {
                launch {
                    async(Dispatchers.IO) {
                        runCatching {
                            Client()
                                .twitterClient(BuildConfig.baseURL)
                                .create(TwitterService::class.java)
                                .signing()
                        }.onSuccess { result ->
                            runCatching {
                                Client()
                                    .twitter(BuildConfig.baseURL, "")
                                    .create(TwitterService::class.java)
                                    .requestToken()
                            }.onSuccess {
                                TwitterLogInBottomSheet
                                    .Builder()
                                    .build()
                                    .show(supportFragmentManager, "TWITTER")
                            }.onFailure {
                                Log.d("OkHttp", "Failed:  ${it.message}")
                            }
                        }.onFailure {
                            Log.d("OkHttp", "Failed:  ${it.message}")
                        }
                    }
                }
            }
        }
        test.setText("1258")
        test.setOnFocusChangeListener { view, b ->
            Log.d("TAG", b.toString())
        }
        findViewById<MaterialButton>(R.id.start_service).setOnClickListener {
            startService(Intent(this, TestService::class.java))
        }
        findViewById<MaterialButton>(R.id.no_password).setOnClickListener {
            launch {
                async(Dispatchers.IO) {
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
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }
}