package com.yosefmoq.firstcode.constant

import com.yosefmoq.firstcode.network.FirstCodeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL: String = "https://jsonplaceholder.typicode.com/"
var retrofit = Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create()).build()
var firstCodeAppApi: FirstCodeApi = retrofit.create(FirstCodeApi::class.java)
const val SERVER_ERROR_CODE = 500
const val COLLECTION_POST = "posts"
const val ADD_POST = "Add Post"
const val EDIT_POST = "Edit Post"
