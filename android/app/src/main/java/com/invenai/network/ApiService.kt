
package com.invenai.network

import com.invenai.model.Product
import com.invenai.model.LoginRequest
import com.invenai.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body params: LoginRequest): LoginResponse

    @GET("products")
    suspend fun getProducts(@Header("Authorization") token: String): List<Product>

    @POST("products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Product

    @POST("ai/describe")
    suspend fun aiDescribe(
        @Header("Authorization") token: String,
        @Body body: Map<String, Any>
    ): Map<String, Any>

    @POST("ai/forecast")
    suspend fun aiForecast(
        @Header("Authorization") token: String,
        @Body body: Map<String, Any>
    ): Map<String, Any>
}
