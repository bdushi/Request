package com.example.core

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLDecoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.HashMap
import kotlin.math.abs

class Client {
    fun retrofit() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://sto-tech-techssona.bde.local/Techsson.Customer.Management.Api/api/Customer/")
            .client(getUnsafeOkHttpClient(logging()))
            .addConverterFactory(converterFactory())
            .build()
    }

    fun twitterClient(host: String) : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(host)
            .client(okHttpClient(logging()))
            .addConverterFactory(converterFactory())
            .build()
    }


    // @url https://api.twitter.com/oauth/request_token
    // OAuth oauth_consumer_key="ETSFMgKDvsZAAUPdoev7ai8zp",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1624022923",oauth_nonce="667348460",oauth_version="1.0",oauth_token="",oauth_signature="ovT9j70Bd4WVzDrvdG7h691pL2k%3D"
    fun twitter(host: String) : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(host)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addConverterFactory(converterFactory())
            .build()
    }

    fun signing(host: String, apiKey: String, secretApiKey: String,): Retrofit {
        val timestamp = System.currentTimeMillis() / 1000
        val data = timestamp + abs(Random().nextInt())
        return Retrofit
            .Builder()
            .baseUrl(host)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.HEADERS
                    })
                    .addInterceptor {
                        val ongoing: Request.Builder = it.request().newBuilder()
                        ongoing.addHeader("include_entities", "true")
                        ongoing.addHeader("oauth_consumer_key", apiKey)
                        ongoing.addHeader("oauth_nonce", BASE64Encoder.encode(data.toString().toByteArray()))
                        ongoing.addHeader("oauth_signature_method", "HMAC-SHA1")
                        ongoing.addHeader("oauth_timestamp", "$timestamp")
                        ongoing.addHeader("oauth_token", secretApiKey)
                        ongoing.addHeader("oauth_signature_method", "HMAC-SHA1")
                        it.proceed(ongoing.build())
                    }.build()
            )
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