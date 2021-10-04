package com.example.core

import com.example.core.Common.encode
import com.example.core.Common.encodeParameters
import com.example.core.Common.generateSignature
import com.example.core.Common.normalizeRequestParameters
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLDecoder
import java.util.*

// Original Header
// OAuth oauth_consumer_key="ETSFMgKDvsZAAUPdoev7ai8zp",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1627477226",oauth_nonce="3541557976",oauth_version="1.0",oauth_signature="cPoHbDIAhYj4xOI8380BDwi5FAE%3D"
// OAuth oauth_consumer_key="ETSFMgKDvsZAAUPdoev7ai8zp",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1627483685",oauth_nonce="1799048480",oauth_version="1.0",oauth_signature="DkfT518oXuSx2o0qHPlKhcF5MJM%3D"

data class Header(
    private val method: String,
    private val apiKey: String,
    private val secretApiKey: String,
    private val oauthToken: OAuthToken?,
    private val url :String,
    private val RAND: Int,
    private val timestamp: Long) {

    private val nonce = timestamp + RAND

    private val oauthHeaderParams = arrayListOf(
        HttpParameter("oauth_consumer_key", apiKey),
        HttpParameter("oauth_signature_method", HMAC_SHA1),
        HttpParameter("oauth_timestamp", timestamp.toString()),
        HttpParameter("oauth_nonce", nonce.toString()),
        HttpParameter("oauth_version", "1.0"),
        HttpParameter("oauth_callback", "brddemo://")
    )

    fun header(): String {
        if(oauthToken != null) {
            oauthHeaderParams.add(HttpParameter("oauth_token", oauthToken.token))
        }

        val signatureBaseParams = arrayListOf<HttpParameter>()
        signatureBaseParams.addAll(oauthHeaderParams)
        parseGetParameters(
            url,
            signatureBaseParams
        )

        val base: StringBuilder =
            StringBuilder(method).append("&")
                .append(encode(Common.constructRequestURL(url)))
                .append("&")
        base.append(
            encode(normalizeRequestParameters(signatureBaseParams))
        )
        val oauthBaseString = base.toString()
        val signature: String = generateSignature(
            oauthBaseString,
            null,
            secretApiKey
        )
        oauthHeaderParams.add(HttpParameter("oauth_signature", signature))
        return "OAuth " + encodeParameters(oauthHeaderParams, ",", true)
    }

    private fun parseGetParameters(url: String, signatureBaseParams: MutableList<HttpParameter>) {
        val queryStart = url.indexOf("?")
        if (-1 != queryStart) {
            val queryStrs = url.substring(queryStart + 1).split("&").toTypedArray()
            try {
                for (query in queryStrs) {
                    val split = query.split("=").toTypedArray()
                    if (split.size == 2) {
                        signatureBaseParams.add(
                            HttpParameter(
                                URLDecoder.decode(
                                    split[0],
                                    "UTF-8"
                                ), URLDecoder.decode(
                                    split[1],
                                    "UTF-8"
                                )
                            )
                        )
                    } else {
                        signatureBaseParams.add(
                            HttpParameter(
                                URLDecoder.decode(
                                    split[0],
                                    "UTF-8"
                                ), ""
                            )
                        )
                    }
                }
            } catch (ignore: UnsupportedEncodingException) {
            }
        }
    }
    private fun parseGetParameters(url: String, signatureBaseParams: HashMap<String, String>) {
        val queryStart = url.indexOf("?")
        if (-1 != queryStart) {
            val queryStrs = url.substring(queryStart + 1).split("&").toTypedArray()
            try {
                for (query in queryStrs) {
                    val split = query.split("=").toTypedArray()
                    if (split.size == 2) {
                        URLDecoder.decode(split[0], "UTF-8")
                        signatureBaseParams[URLDecoder.decode(split[0], "UTF-8")] =
                            URLDecoder.decode(split[1], "UTF-8")
                    } else {
                        signatureBaseParams[URLDecoder.decode(split[0], "UTF-8")] = ""
                    }
                }
            } catch (ignore: UnsupportedEncodingException) {
            }
        }
    }
}