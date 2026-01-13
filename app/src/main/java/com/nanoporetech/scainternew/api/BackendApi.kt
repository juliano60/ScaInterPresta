package com.nanoporetech.scainternew.api

import com.google.gson.GsonBuilder
import com.nanoporetech.scainternew.model.FetchProviderRequest
import com.nanoporetech.scainternew.model.Provider
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApi {
    @POST("/provider_api.php")
    suspend fun fetchProvider(@Body request: FetchProviderRequest): Response<Provider>
}

class RetrofitInstance {
    companion object {
        val BASE_URL = "http://138.68.160.209/centredesantetout/"
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}