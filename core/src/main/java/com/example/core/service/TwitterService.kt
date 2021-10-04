package com.example.core.service

import retrofit2.http.*

/**
 * https://developer.twitter.com/en/docs/authentication/api-reference/request_token
 *
 * signing: https://developer.twitter.com/en/docs/authentication/oauth-1-0a/creating-a-signature
 *
 * https://developer.twitter.com/en/docs/authentication/oauth-1-0a/authorizing-a-request
 *
 * https://developer.twitter.com/en/docs/authentication/oauth-1-0a/creating-a-signature
 */

interface TwitterService {
    // "X-Twitter-Client-Version", "4.0.7"
    // X-Twitter-Client-URL, http://twitter4j.org/en/twitter4j-4.0.7.xml
    // X-Twitter-Client, Twitter4J
//    @Headers(
//        "X-Twitter-Client-Version : 4.0.7",
//        "X-Twitter-Client-URL : http://twitter4j.org/en/twitter4j-4.0.7.xml",
//        "X-Twitter-Client : Twitter4J",
//        "User-Agent : twitter4j http://twitter4j.org/ /"
//    )
    @Headers("Content-Type:application/json")
    @POST("oauth/request_token/")
    suspend fun requestToken(@Header("Authorization") header: String)

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("1.1/statuses/update.json?include_entities=true")
    suspend fun signing()
}