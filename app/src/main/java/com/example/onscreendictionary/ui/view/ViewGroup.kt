package com.example.onscreendictionary.ui.view

import android.view.View
import android.view.ViewGroup

inline fun View.findParent(predicate: (ViewGroup) -> Boolean): ViewGroup? {
    var result = parent as? ViewGroup ?: return null
    while (true) {
        if (predicate(result)) {
            return result
        }
        result = result.parent as? ViewGroup ?: return null
    }
}