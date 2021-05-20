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

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
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
                } else {
                    txtInputLayout.error = null
                    txtInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        findViewById<MaterialButton>(R.id.no_password).setOnClickListener {
            launch {
                async(Dispatchers.IO) {
                    runCatching {
                        retrofit()
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


    private fun retrofit() : Retrofit {
        return Retrofit
                .Builder()
                .baseUrl("https://sto-tech-techssona.bde.local/Techsson.Customer.Management.Api/api/Customer/")
                .client(getUnsafeOkHttpClient(logging()))
                .addConverterFactory(converterFactory())
                .build()
    }

    private fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    private fun logging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private fun converterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
                GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create()
        )
    }

    private fun getUnsafeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?)  = Unit
                        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?)  = Unit
                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            OkHttpClient
                    .Builder()
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { p0, p1 -> true }
                    .addInterceptor(httpLoggingInterceptor)
                    .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}