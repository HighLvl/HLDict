package com.example.onscreendictionary.ui.word_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.domain.repository.DictionaryRepository
import com.example.onscreendictionary.ui.base.BaseViewModel
import com.example.onscreendictionary.ui.base.ProgressData
import com.example.onscreendictionary.ui.base.launch
import com.example.onscreendictionary.ui.base.launchAndSetFlow
import com.example.onscreendictionary.ui.data.FormattedWordDefinition
import com.example.onscreendictionary.ui.data.toFormatted
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordDetailsViewModel @Inject constructor(
    private val coordinator: WordDetailsCoordinator,
    private val dictionaryRepository: DictionaryRepository
) : BaseViewModel() {
    override val args: WordDefinitionId get() = args()
    private val _wordDetails = MutableLiveData<ProgressData<FormattedWordDefinition>>()
    val wordDetails: LiveData<ProgressData<FormattedWordDefinition>> get() = _wordDetails
    override fun onCreate() {
        super.onCreate()

        load()
    }

    private fun load() {
        launchAndSetFlow(_wordDetails) {
            dictionaryRepository.getFullWordDefinition(args, false).map {
                it.toFormatted()
            }
        }
    }

    fun retry() {
        load()
    }

    fun setFavorite(isFavorite: Boolean) {
        launch {
            dictionaryRepository.setFavorite(args, isFavorite)
        }
    }

    override fun back() {
        coordinator.back()
    }
}