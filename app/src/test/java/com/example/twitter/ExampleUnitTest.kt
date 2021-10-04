package com.example.twitter

import com.example.core.Client
import com.example.core.Header
import com.example.core.service.TwitterService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ExampleUnitTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun twitter() = runBlocking {
        val client = Client().twitter("https://api.twitter.com/")
        val header = Header("POST").header
        val response = client.create(TwitterService::class.java).requestToken(header)
        assertNotNull(response)
    }
}