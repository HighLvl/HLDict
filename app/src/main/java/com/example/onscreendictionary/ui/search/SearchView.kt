package com.example.onscreendictionary.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.onscreendictionary.databinding.SearchViewBinding


class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: SearchViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = SearchViewBinding.inflate(inflater, this)
    }

    fun setOnSearchClickListener(l: OnClickListener) {
        binding.searchContainer.setOnClickListener(l)
    }

    fun setQuery(query: String) {
        binding.queryTextView.text = query
    }

}