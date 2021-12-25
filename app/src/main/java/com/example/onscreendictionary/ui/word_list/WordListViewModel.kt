package com.example.onscreendictionary.ui.word_list

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordListViewModel @Inject constructor(
    private val coordinator: WordListCoordinator,
    private val repository: DictionaryRepository
) : BaseViewModel() {
    override val args: WordListArgs get() = args()
    private val _wordList = MutableLiveData<ProgressData<List<FormattedWordDefinition>>>()
    val wordList: LiveData<ProgressData<List<FormattedWordDefinition>>>
        get() = _wordList

    private val _wordTitle = MutableLiveData("")
    val wordTitle: LiveData<String>
        get() = _wordTitle

    private var retry: (() -> Unit)? = null
    private var getWordDefJob: Job? = null

    override fun onCreate() {
        super.onCreate()

        loadWordDefinitions(args.query)
    }

    private fun loadWordDefinitions(query: String?) {
        retry = { loadWordDefinitions(query) }
        _wordTitle.value = query.orEmpty()
        getWordDefJob?.cancel()
        getWordDefJob = launchAndSetFlow(_wordList) {
            wordDefinitionsFlow(query).map { formattedWords ->
                _wordTitle.value = if (!args.favoriteOnly || args.query != null) {
                    formattedWords.firstOrNull()?.title?.toString().orEmpty().also {
                        coordinator.updateArgs(query = it)
                    }
                } else {
                    ""
                }
                formattedWords
            }
        }
    }

    private fun wordDefinitionsFlow(query: String?): Flow<List<FormattedWordDefinition>> {
        return if (query == null && !args.favoriteOnly) {
            repository.getRandomWordDefinitions(args.favoriteOnly)
        } else {
            repository.getShortWordDefinitions(query.orEmpty(), args.favoriteOnly, false)
        }
            .map { list -> list.map { it.toFormatted() } }
    }

    fun randomWord() {
        loadWordDefinitions(null)
    }

    fun retry() {
        retry?.invoke()
    }

    fun search() {
        coordinator.search()
    }

    fun wordDetails(id: WordDefinitionId) {
        coordinator.details(id)
    }

    fun setFavorite(id: WordDefinitionId, isFavorite: Boolean) {
        launch {
            repository.setFavorite(id, isFavorite)
        }
    }

    override fun back() {
        coordinator.back()
    }
}