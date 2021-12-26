package com.example.onscreendictionary.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onscreendictionary.di.base.BaseComponent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    lateinit var component: BaseComponent
    var _args: Args? = null

    @Suppress("UNCHECKED_CAST")
    protected fun <T : Args> args(): T {
        return _args as T
    }

    protected open val args: Args get() = args()

    private var isCreated = false
    fun attach() {
        if (!isCreated) {
            onCreate()
            isCreated = true
        }
    }

    /** on create viewModel */
    protected open fun onCreate() {
    }

    /** on destroy viewModel */
    protected open fun onDestroy() {
    }

    open fun back() {
    }

    final override fun onCleared() {
        onDestroy()
        isCreated = false
    }
}

fun <T : Any> BaseViewModel.launch(
    errorReceiver: MutableLiveData<Throwable>? = null,
    block: suspend () -> T
): Job {
    return viewModelScope.launch {
        try {
            block()
        } catch (e: CancellationException) {
        } catch (e: Throwable) {
            val viewModelName = this@launch::class.qualifiedName
            Log.d("BaseViewModel", "exception in $viewModelName", e)
            errorReceiver?.value = e
        }
    }
}

fun <T : Any> BaseViewModel.launchAndSetResult(
    receiver: MutableLiveData<ProgressData<T>>,
    block: suspend () -> T
): Job {
    receiver.value = (receiver.value ?: ProgressData()).copy(isProgress = true, error = null)
    return viewModelScope.launch {
        try {
            val data = block()
            receiver.value = receiver.value!!.copy(isProgress = false, data = data, error = null)
        } catch (e: CancellationException) {
        } catch (e: Throwable) { // maybe custom handler for CancellationException?
            val viewModelName = this@launchAndSetResult::class.qualifiedName
            Log.d("BaseViewModel", "exception in $viewModelName", e)
            receiver.value = receiver.value!!.copy(isProgress = false, data = null, error = e)
        }
    }
}

fun <T : Any> BaseViewModel.launchAndSetFlow(
    receiver: MutableLiveData<ProgressData<T>>,
    block: () -> Flow<T>
): Job {
    receiver.value = (receiver.value ?: ProgressData()).copy(isProgress = true, error = null)
    return viewModelScope.launch {
        try {
            block().collect { data ->
                receiver.value =
                    receiver.value!!.copy(isProgress = false, data = data, error = null)
            }
        } catch (e: CancellationException) {
        } catch (e: Throwable) {
            val viewModelName = this@launchAndSetFlow::class.qualifiedName
            Log.d("BaseViewModel", "exception in $viewModelName", e)
            receiver.value = receiver.value!!.copy(isProgress = false, data = null, error = e)
        }
    }
}