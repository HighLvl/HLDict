package com.example.onscreendictionary.ui.search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import com.example.onscreendictionary.databinding.SearchFragmentBinding
import com.example.onscreendictionary.ui.base.BaseFragment
import com.example.onscreendictionary.ui.view.hideKeyboard
import com.example.onscreendictionary.ui.view.setRetryError
import com.example.onscreendictionary.ui.view.showKeyboard

class SearchFragment(
    override val args: SearchArgs? = null
) : BaseFragment() {
    override val viewModel: SearchViewModel by impl()
    override val viewBinding: SearchFragmentBinding by impl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        searchView.apply {
            doOnLayout { showKeyboard() }
            val layout = findViewById<LinearLayout>(androidx.appcompat.R.id.search_edit_frame)
            val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
            layoutParams.leftMargin = 0

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.queryChanged(newText)
                    return true
                }
            })
        }

        toolbar.setNavigationOnClickListener {
            viewModel.back()
        }

        val suggestionAdapter = SuggestionRecyclerViewAdapter { suggestion ->
            viewModel.selectWordTitle(suggestion)
        }
        suggestionsRecyclerView.adapter = suggestionAdapter

        viewModel.query.observe(viewLifecycleOwner) {
            if (searchView.query.toString() != it) {
                searchView.setQuery(it, false)
            }
        }
        viewModel.wordTitles.observe(viewLifecycleOwner) {
            progressBar.isVisible = it.isProgress
            errorTextView.setRetryError(it.error) {
                viewModel.retry()
            }
            suggestionsRecyclerView.isVisible = !it.isProgress
            suggestionAdapter.dataList = it.data.orEmpty()
        }
    }

    override fun onPause() {
        super.onPause()
        viewBinding.root.hideKeyboard()
    }
}