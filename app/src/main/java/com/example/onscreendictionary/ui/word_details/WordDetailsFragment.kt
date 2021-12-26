package com.example.onscreendictionary.ui.word_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.onscreendictionary.R
import com.example.onscreendictionary.databinding.WordDetailsFragmentBinding
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.ui.base.BaseFragment
import com.example.onscreendictionary.ui.view.localizedMessageErrorMapper
import com.example.onscreendictionary.ui.view.setRetryError

class WordDetailsFragment(
    override val args: WordDefinitionId? = null
) : BaseFragment() {
    override val viewBinding: WordDetailsFragmentBinding by impl()
    override val viewModel: WordDetailsViewModel by impl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wordDetails.observe(viewLifecycleOwner) { word ->
            nonCriticalErrorTextView.setRetryError(
                if (word.data != null && !word.data.isFull) {
                    Throwable(resources.getString(R.string.wordDetailsNotFullError))
                } else {
                    null
                },
                localizedMessageErrorMapper
            ) {
                viewModel.retry()
            }
            progressBar.isVisible = word.isProgress
            errorTextView.setRetryError(word.error) {
                viewModel.retry()
            }
            scrollView.isVisible = word.data != null
            if (word.data == null) {
                return@observe
            }
            val wordDef = word.data
            titleTextView.text = wordDef.title
            numTextView.text = wordDef.num
            posTextView.text = wordDef.pos
            glossTextView.text = wordDef.gloss
            syllablesTextView.text = wordDef.syllables
            langTextView.text = wordDef.lang
            examplesTextView.text = wordDef.examplesText
            examplesContainer.visibility = wordDef.examplesVisibility
            synonymsTextView.text = wordDef.synonyms
            synonymsContainer.visibility = wordDef.synonymsVisibility
            antonymsTextView.text = wordDef.antonyms
            antonymsContainer.visibility = wordDef.antonymsVisibility
            hyponymsTextView.text = wordDef.hyponyms
            hyponymsContainer.visibility = wordDef.hyponymsVisibility
            hypernymsTextView.text = wordDef.hypernyms
            hypernymsContainer.visibility = wordDef.hypernymsVisibility
            etymologyTextView.text = wordDef.etymology
            etymologyContainer.visibility = wordDef.etymologyVisibility
            phrasTextView.text = wordDef.phras
            phrasContainer.visibility = wordDef.phrasVisibility
            ipaTextView.text = wordDef.ipa
            ipaContainer.visibility = wordDef.ipaVisibility
            favoriteButton.setImageResource(
                if (wordDef.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border_black
            )
            favoriteButton.setOnClickListener {
                viewModel.setFavorite(!wordDef.isFavorite)
            }
        }

        toolbar.setNavigationOnClickListener {
            viewModel.back()
        }
    }
}