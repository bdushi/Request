package com.example.core

import java.io.UnsupportedEncodingException
import java.lang.AssertionError
import java.lang.StringBuilder
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.HashMap
import com.example.core.HttpParameter.encodeParameters
import com.example.core.HttpParameter.encodeParameters

object Common {
    /**
     * Computes RFC 2104-compliant HMAC signature.
     *
     * @param data  the data to be signed
     * @param token the token
     * @return signature
     * @see [OAuth Core - 9.2.1.  Generating Signature](http://oauth.net/core/1.0a/.rfc.section.9.2.1)
     */
    fun generateSignature(data: String, token: OAuthToken?, consumerSecret: String): String {
        val byteHMAC: ByteArray?
        try {
            val mac = Mac.getInstance(HMAC_SHA1)
            var spec: SecretKeySpec
            if (null == token) {
                val oauthSignature: String = encode(consumerSecret) + "&"
                spec = SecretKeySpec(oauthSignature.toByteArray(), HMAC_SHA1)
            } else {
                spec = token.secretKeySpec
                if (null == spec) {
                    val oauthSignature: String = encode(consumerSecret) + "&" + encode(token.tokenSecret)
                    spec = SecretKeySpec(
                        oauthSignature.toByteArray(), HMAC_SHA1
                    )
                    token.secretKeySpec = spec
                }
            }
            mac.init(spec)
            byteHMAC = mac.doFinal(data.toByteArray())
        } catch (ike: InvalidKeyException) { 
            System.err.printf("Failed initialize \"Message Authentication Code\" (MAC)", ike)
            throw AssertionError(ike)
        } catch (nsae: NoSuchAlgorithmException) {
            System.err.printf("Failed to get HmacSHA1 \"Message Authentication Code\" (MAC)", nsae)
            throw AssertionError(nsae)
        }
        return BASE64Encoder.encode(byteHMAC)
    }

    fun constructRequestURL(url: String): String {
        var url = url
        val index = url.indexOf("?")
        if (-1 != index) {
            url = url.substring(0, index)
        }
        val slashIndex = url.indexOf("/", 8)
        var baseURL = url.substring(0, slashIndex).lowercase()
        val colonIndex = baseURL.indexOf(":", 8)
        if (-1 != colonIndex) {
            // url contains port number
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                // http default port 80 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex)
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                // http default port 443 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex)
            }
        }
        return baseURL + url.substring(slashIndex)
    }

    /**
     * @param value string to be encoded
     * @return encoded string
     * @see <a href="http://wiki.oauth.net/TestCases">OAuth / TestCases</a>
     * @see <a href="http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding#9d79b698ab217df2">Space encoding - OAuth | Google Groups</a>
     * @see <a href="http://tools.ietf.org/html/rfc3986#section-2.1">RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax - 2.1. Percent-Encoding</a>
     */
    fun encode(value: String?): String {
        var encoded: String? = null
        try {
            encoded = URLEncoder.encode(value, "UTF-8")
        } catch (ignore: UnsupportedEncodingException) {
        }
        val buf = StringBuilder(encoded!!.length)
        var focus: Char
        var i = 0
        while (i < encoded.length) {
            focus = encoded[i]
            if (focus == '*') {
                buf.append("%2A")
            } else if (focus == '+') {
                buf.append("%20")
            } else if (focus == '%' && i + 1 < encoded.length && encoded[i + 1] == '7' && encoded[i + 2] == 'E') {
                buf.append('~')
                i += 2
            } else {
                buf.append(focus)
            }
            i++
        }
        return buf.toString()
    }

    fun normalizeRequestParameters(params: HashMap<String, String>): String {
        params.toSortedMap()
        return encodeParameters(params)
    }

    fun normalizeRequestParameters(params: List<HttpParameter>): String {
        val sorted = params.sortedBy { it.name }
        return encodeParameters(sorted)
    }


    /**
     * @param httpParams parameters to be encoded and concatenated
     * @return encoded string
     * @see [OAuth / TestCases](http://wiki.oauth.net/TestCases)
     *
     * @see [Space encoding - OAuth | Google Groups](http://groups.google.com/group/oauth/browse_thread/thread/a8398d0521f4ae3d/9d79b698ab217df2?hl=en&lnk=gst&q=space+encoding.9d79b698ab217df2)
     */
    private fun encodeParameters(httpParams: HashMap<String, String>): String {
        return encodeParameters(httpParams, "&", false)
    }

    private fun encodeParameters(httpParams: List<HttpParameter>): String {
        return encodeParameters(httpParams, "&", false)
    }

    private fun encodeParameters(
        httpParams: HashMap<String, String>,
        splitter: String?,
        quot: Boolean
    ): String {
        val buf = StringBuilder()
        for (param in httpParams) {
            if (buf.isNotEmpty()) {
                if (quot) {
                    buf.append("\"")
                }
                buf.append(splitter)
            }
            buf.append(encode(param.key) + ("="))
            if (quot) {
                buf.append("\"")
            }
            buf.append(encode(param.value))
        }
        if (buf.isNotEmpty()) {
            if (quot) {
                buf.append("\"")
            }
        }
        return buf.toString()
    }

    fun encodeParameters(
        httpParams: List<HttpParameter>,
        splitter: String,
        quot: Boolean
    ): String {
        val buf = StringBuilder()
        for (param in httpParams) {
            if (!param.isFile) {
                if (buf.isNotEmpty()) {
                    if (quot) {
                        buf.append("\"")
                    }
                    buf.append(splitter)
                }
                buf.append(HttpParameter.encode(param.name)).append("=")
                if (quot) {
                    buf.append("\"")
                }
                buf.append(HttpParameter.encode(param.value))
            }
        }
        if (buf.isNotEmpty()) {
            if (quot) {
                buf.append("\"")
            }
        }
        return buf.toString()
    }
}