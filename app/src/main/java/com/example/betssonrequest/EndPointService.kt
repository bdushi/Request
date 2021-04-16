package com.example.betssonrequest

import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query

interface EndPointService {
    @Headers(
            "accept: */*",
            "brand: Betsson",
            "jurisdiction: SGA"
    )
    @PUT("SetNoPasswordCredential")
    suspend fun noPassword(@Query("customerId") customerId: String) : Response<Unit>
}