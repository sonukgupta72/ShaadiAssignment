package com.sonukgupta72.shaadiassignment.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/")
    fun getProfiles(@Query("results") results: Int): Single<ProfileResponseDataModule>

    @GET("/api/")
    suspend fun getProfilesCr(@Query("results") results: Int): ProfileResponseDataModule
}