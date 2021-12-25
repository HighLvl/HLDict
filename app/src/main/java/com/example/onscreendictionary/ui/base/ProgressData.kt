package com.example.onscreendictionary.ui.base

data class ProgressData<T: Any>(
    val isProgress: Boolean = false,
    val data: T? = null,
    val error: Throwable? = null
)