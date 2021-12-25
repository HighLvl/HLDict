package com.example.onscreendictionary.ui.view

import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.onscreendictionary.R
import okio.EOFException
import okio.IOException

fun TextView.setRetryError(
    t: Throwable?,
    mapper: (resources: Resources, e: Throwable) -> String = defaultErrorMapper,
    retry: () -> Unit
) {
    isVisible = t != null
    if (t == null) {
        return
    }
    val sb = SpannableStringBuilder(mapper(resources, t))
    sb.append("\n\n")
    val spanStart = sb.length
    sb.append("Повторить")
    val retrySpan = object : ClickableSpan() {
        override fun onClick(v: View) {
            retry()
        }
    }
    sb.setSpan(retrySpan, spanStart, sb.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    movementMethod = LinkMovementMethod.getInstance()
    text = sb
}

val localizedMessageErrorMapper = { resources: Resources, e: Throwable ->
    e.localizedMessage ?: resources.getString(R.string.appErrorDefaultTitle)
}

val defaultErrorMapper = { resources: Resources, e: Throwable ->
    if (e is IOException && e !is EOFException) {
        resources.getString(R.string.appErrorNetworkTitle)
    } else {
        resources.getString(R.string.appErrorServerTitle)
    }
}