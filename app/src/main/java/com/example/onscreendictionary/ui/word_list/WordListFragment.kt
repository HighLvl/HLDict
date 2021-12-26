package com.example.onscreendictionary.ui.word_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.onscreendictionary.R
import com.example.onscreendictionary.databinding.WordListFragmentBinding
import com.example.onscreendictionary.ui.base.BaseFragment
import com.example.onscreendictionary.ui.view.setRetryError


class WordListFragment(
    override val args: WordListArgs? = null
) : BaseFragment() {
    override val viewBinding: WordListFragmentBinding by impl()
    override val viewModel: WordListViewModel by impl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        if (args!!.favoriteOnly) {
            viewBinding.searchView.setHint(getString(R.string.wordListSearchHintFavorite))
        }

        val recyclerViewAdapter = WordListRecyclerViewAdapter(
            viewModel::wordDetails,
            viewModel::setFavorite
        )

        wordListRecyclerView.adapter = recyclerViewAdapter
        searchView.setOnSearchClickListener {
            viewModel.search()
        }
        randomWordButton.isVisible = !args.favoriteOnly
        randomWordButton.setOnClickListener {
            viewModel.randomWord()
        }

        viewModel.wordList.observe(viewLifecycleOwner) {
            progressBar.isVisible = it.isProgress
            errorTextView.setRetryError(it.error) {
                viewModel.retry()
            }
            wordListRecyclerView.isVisible = !it.isProgress

            recyclerViewAdapter.data = it.data.orEmpty()
        }

        viewModel.wordTitle.observe(viewLifecycleOwner) {
            searchView.setQuery(it)
        }
    }
}