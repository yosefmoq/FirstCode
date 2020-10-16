package com.yosefmoq.firstcode.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

class ConnectionLiveData(val context: Context) : LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    private val networkRequestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
        .addTransportType(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
                getConnectivityMarshmallowManagerCallback()
            )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> marshmallowNetworkAvailableRequest()
            else -> {
                    context.registerReceiver(
                        networkReceiver,
                        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                    ) // android.net.ConnectivityManager.CONNECTIVITY_ACTION

            }
        }
    }

    override fun onInactive() {
        super.onInactive()
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }


    private fun marshmallowNetworkAvailableRequest() {
        connectivityManager.registerNetworkCallback(
            networkRequestBuilder.build(),
            getConnectivityMarshmallowManagerCallback()
        )
    }

    private fun getConnectivityLollipopManagerCallback(): ConnectivityManager.NetworkCallback {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }
            }
            return connectivityManagerCallback
    }

    private fun getConnectivityMarshmallowManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    networkCapabilities?.let { capabilities ->
                        if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_VALIDATED
                            )
                        ) {
                            postValue(true)
                        }
                    }
                }

                override fun onLost(network: Network) {
                    postValue(false)
                }
            }
            return connectivityManagerCallback
        } else {
            throw IllegalAccessError("Accessing wrong API version")
        }


    }
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)
    }
}