package me.tylerbwong.stack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    val refreshing: LiveData<Boolean>
        get() = _refreshing
    private val _refreshing = MutableLiveData<Boolean>()

    val snackbar: LiveData<Unit?>
        get() = mutableSnackbar
    protected val mutableSnackbar = SingleLiveEvent<Unit?>()

    protected fun launchRequest(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            try {
                _refreshing.value = true
                mutableSnackbar.value = null
                block()
            } catch (exception: Exception) {
                Timber.e(exception)
                mutableSnackbar.value = Unit
            } finally {
                _refreshing.value = false
            }
        }
    }

    protected fun <T> streamRequest(source: Flow<T>, onReceive: suspend (T) -> Unit) {
        viewModelScope.launch {
            _refreshing.value = true
            try {
                _refreshing.value = true
                source.collect {
                    onReceive(it)
                    _refreshing.value = false
                }
            } catch (exception: Exception) {
                Timber.e(exception)
                mutableSnackbar.value = Unit
            }
        }
    }

    companion object {
        const val SHARE_TEXT_TYPE = "text/plain"
    }
}
