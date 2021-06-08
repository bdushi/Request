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
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.content.Intent
import android.util.Log

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
        val txtInputLayout: TextInputLayout = findViewById(R.id.txtInputLayout)
        val txtInput: TextInputEditText = findViewById(R.id.txtInput)
        txtInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0.isNullOrEmpty()) {
                    txtInputLayout.error = "Error"
                    txtInput.setHintTextColor(myList)
//                    txtInputLayout.hintTextColor = myList
//                    txtInputLayout.setHintTextAppearance(R.style.HintTextAppearance)
                } else {
                    txtInputLayout.error = null
                    txtInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
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