package com.example.core

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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

    fun twitter(host: String, oauthNonce: String, nonce: String, timestamp: String) : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(host)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.HEADERS
                    })
                    .addInterceptor(fun(chain: Interceptor.Chain): Response {
                        return chain.proceed(
                            chain.request()
                                .newBuilder()
                                .addHeader("OAuth oauth_consumer_key", "ETSFMgKDvsZAAUPdoev7ai8zp")
                                .addHeader("oauth_signature_method", "HMAC-SHA1")
                                .addHeader("oauth_timestamp", timestamp)
                                .addHeader("oauth_signature", oauthNonce)
                                .addHeader("oauth_nonce", nonce)
                                .build()
                        )
                    }).build()
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