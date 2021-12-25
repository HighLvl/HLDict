package com.example.onscreendictionary.ui.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard() {
    requestFocus()
    val currentFocus = findFocus() ?: return
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val currentFocus = findFocus()
    if (currentFocus != null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}