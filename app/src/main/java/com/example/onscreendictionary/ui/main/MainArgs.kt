package com.example.onscreendictionary.ui.main

import com.example.onscreendictionary.ui.base.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainArgs(
    val query: String?
): Args