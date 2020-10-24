package me.tylerbwong.stack.ui.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.distinctUntilChanged

class ConnectivityChecker(private val manager: ConnectivityManager) : LifecycleObserver {

    private var isMonitoringConnectivity = false

    val connectedState: LiveData<Boolean>
        get() = _connectedState.distinctUntilChanged()
    private val _connectedState = MutableLiveData<Boolean>()

    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _connectedState.postValue(true)
        }

        override fun onLost(network: Network) {
            _connectedState.postValue(false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopMonitoringConnectivity() {
        if (isMonitoringConnectivity) {
            manager.unregisterNetworkCallback(connectivityCallback)
            isMonitoringConnectivity = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startMonitoringConnectivity() {
        manager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            connectivityCallback
        )
        isMonitoringConnectivity = true
    }
}
