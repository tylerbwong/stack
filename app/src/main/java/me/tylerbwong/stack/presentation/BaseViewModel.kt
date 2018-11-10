package me.tylerbwong.stack.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    val refreshing: LiveData<Boolean>
        get() = _refreshing
    private val _refreshing = MutableLiveData<Boolean>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    protected fun launchRequest(block: suspend () -> Unit): Job {
        return uiScope.launch {
            try {
                _refreshing.value = true
                block()
            }
            catch (exception: Exception) {
                Timber.e(exception)
            }
            finally {
                _refreshing.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
