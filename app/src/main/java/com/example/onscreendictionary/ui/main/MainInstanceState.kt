package com.example.onscreendictionary.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainInstanceState(
    val sections: Map<MainBottomSection, MutableList<MainRoute>>,
    // fails with kotlin.Pair<MainBottomSection, MainRoute>
    val sectionKey: MainBottomSection,
    val sectionValue: MainRoute
) : Parcelable