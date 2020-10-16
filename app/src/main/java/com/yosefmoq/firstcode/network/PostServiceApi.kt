package com.yosefmoq.firstcode.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yosefmoq.firstcode.constant.BASE_URL
import com.yosefmoq.firstcode.database.PostData
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostServiceApi {

    private var api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(FirstCodeApi::class.java)

    fun getPosts(): Single<List<PostData>> {
        return api.getPosts()
    }
}