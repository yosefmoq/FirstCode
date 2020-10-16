package com.yosefmoq.firstcode.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yosefmoq.firstcode.utils.NetworkUtils

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(NetworkUtils.isConnected(context)){

        }else{

        }
    }
}