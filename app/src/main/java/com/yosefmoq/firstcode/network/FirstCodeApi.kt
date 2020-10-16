package com.yosefmoq.firstcode.network

import com.yosefmoq.firstcode.database.PostData
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface FirstCodeApi {
    @Headers("Accept:application/json")
    @GET("photos")
    fun getPosts(): Single<List<PostData>>

}