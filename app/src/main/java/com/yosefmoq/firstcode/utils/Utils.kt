package com.yosefmoq.firstcode.utils

import com.yosefmoq.firstcode.constant.BASE_URL
import com.yosefmoq.firstcode.constant.firstCodeAppApi
import com.yosefmoq.firstcode.constant.retrofit
import com.yosefmoq.firstcode.network.FirstCodeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class Utils{
    companion object{
        fun createUrlAPI() {
//            BASE_URL = createBaseUrl()
            retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            firstCodeAppApi = retrofit.create(FirstCodeApi::class.java)
        }

    }
}