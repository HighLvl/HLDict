package com.example.onscreendictionary.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onscreendictionary.domain.repository.DictionaryRepository
import com.example.onscreendictionary.ui.base.BaseViewModel
import com.example.onscreendictionary.ui.base.ProgressData
import com.example.onscreendictionary.ui.base.launchAndSetResult
import kotlinx.coroutines.Job
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val coordinator: SearchCoordinator,
    private val dictionaryRepository: DictionaryRepository
) : BaseViewModel() {
    override val args: SearchArgs get() = args()
    private var retry: (() -> Unit)? = null
    private var getWordTitlesStartsWithJob: Job? = null
    private val _wordTitles = MutableLiveData<ProgressData<List<String>>>()

    val wordTitles: LiveData<ProgressData<List<String>>>
        get() = _wordTitles

    private val _query = MutableLiveData<String>()
    val query: LiveData<String>
        get() = _query

    override fun onCreate() {
        super.onCreate()

        queryChanged(args.query.orEmpty())
    }

    fun queryChanged(query: String) {
        if (query == _query.value) {
            return
        }
        _query.value = query
        getWordTitlesStartsWithQuery()
    }

    private fun getWordTitlesStartsWithQuery() {
        retry = ::getWordTitlesStartsWithQuery
        getWordTitlesStartsWithJob?.cancel()
        getWordTitlesStartsWithJob = launchAndSetResult(_wordTitles) {
            dictionaryRepository.getWordTitlesStartsWith(_query.value.orEmpty().trim(), args.favoriteOnly)
        }
    }

    fun retry() {
        retry?.invoke()
    }

    fun selectWordTitle(title: String) {
        _query.value = title
        coordinator.wordList(title)
    }

    override fun back() {
        coordinator.back()
    }
}

