package com.example.onscreendictionary.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onscreendictionary.databinding.SearchSuggestionItemBinding

class SuggestionRecyclerViewAdapter(
    private val onSuggestionClick: (suggestion: String) -> Unit
) :
    RecyclerView.Adapter<SuggestionRecyclerViewAdapter.SuggestionViewHolder>() {

    var dataList: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchSuggestionItemBinding.inflate(inflater, parent, false)
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class SuggestionViewHolder(
        private val binding: SearchSuggestionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.apply {
                suggestionTextView.text = text
                suggestionContainer.setOnClickListener {
                    onSuggestionClick(text)
                }
            }

        }
    }
}