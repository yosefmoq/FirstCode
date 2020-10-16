package com.yosefmoq.firstcode.utils

import android.app.Application
import com.yosefmoq.firstcode.utils.Utils.Companion.createUrlAPI

class FirstCodeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createUrlAPI()
    }
}