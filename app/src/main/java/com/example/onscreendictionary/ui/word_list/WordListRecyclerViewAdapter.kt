package com.example.onscreendictionary.ui.word_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onscreendictionary.R
import com.example.onscreendictionary.databinding.WordListItemBinding
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.ui.data.FormattedWordDefinition

class WordListRecyclerViewAdapter(
    private val onWordClick: (id: WordDefinitionId) -> Unit,
    private val onFavoriteClick: (id: WordDefinitionId, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<WordListRecyclerViewAdapter.WordViewHolder>() {

    var data = listOf<FormattedWordDefinition>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WordListItemBinding.inflate(inflater, parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = data[position]
        holder.bind(word, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class WordViewHolder(
        private val binding: WordListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context

        fun bind(word: FormattedWordDefinition, position: Int) = with(binding) {
            syllablesTextView.text = if (word.num != null) {
                context.getString(R.string.wordListItemSyllables, word.num, word.syllables)
            } else {
                word.syllables
            }
            posTextView.text = word.pos
            glossTextView.text = word.gloss
            langTextView.text = word.lang
            examplesTextView.apply {
                text = word.examplesText
                visibility = word.examplesVisibility
            }

            synonymsTextView.text = word.synonyms
            synonymsContainer.visibility = word.synonymsVisibility
            antonymsTextView.text = word.antonyms
            antonymsContainer.visibility = word.antonymsVisibility
            favoriteButton.setImageResource(
                if (word.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border_black
            )
            favoriteButton.setOnClickListener {
                onFavoriteClick(word.id, !word.isFavorite)
            }
            root.setOnClickListener {
                onWordClick(word.id)
            }
        }
    }
}