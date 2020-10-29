package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.distinctUntilChanged
import com.google.android.material.snackbar.Snackbar
import me.tylerbwong.stack.R

fun Context.createConnectivitySnackbar(anchorView: View): Snackbar {
    return Snackbar.make(
        anchorView,
        R.string.no_internet,
        Snackbar.LENGTH_INDEFINITE
    ).apply {
        this.anchorView = anchorView
        setAction(R.string.settings) {
            val setting = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Settings.Panel.ACTION_INTERNET_CONNECTIVITY
            } else {
                Settings.ACTION_WIRELESS_SETTINGS
            }
            startActivity(Intent(setting))
        }
    }
}

class ConnectivityChecker(private val manager: ConnectivityManager) : LifecycleObserver {

    private var isMonitoringConnectivity = false

    val connectedState: LiveData<Boolean>
        get() = _connectedState.distinctUntilChanged()
    private val _connectedState = MutableLiveData<Boolean>()

    private val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = _connectedState.postValue(true)
        override fun onLost(network: Network) = _connectedState.postValue(false)
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
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        manager.registerNetworkCallback(request, connectivityCallback)
        isMonitoringConnectivity = true
    }
}
