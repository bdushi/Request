package com.example.core.service

import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

    @POST("oauth/request_token")
    suspend fun requestToken() {

    }
    @FormUrlEncoded
    @POST("1.1/statuses/update.json")
    fun signing()
}