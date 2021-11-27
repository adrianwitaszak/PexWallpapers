package com.adwi.pexwallpapers.ui.screens.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.adwi.pexwallpapers.data.wallpapers.repository.WallpaperRepository
import com.adwi.pexwallpapers.di.IoDispatcher
import com.adwi.pexwallpapers.model.Wallpaper
import com.adwi.pexwallpapers.ui.base.BaseViewModel
import com.adwi.pexwallpapers.util.ext.onDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel() {

    private val _currentQuery = MutableStateFlow("")

    val currentQuery = _currentQuery.asStateFlow()

    val searchResults = currentQuery.flatMapLatest { query ->
        wallpaperRepository.getSearch(query)
    }.cachedIn(viewModelScope)

    fun onSearchQuerySubmit(query: String) {
        _currentQuery.value = query
        _isRefreshing.value = true
        _pendingScrollToTopAfterRefresh.value = true
        setQuery(currentQuery.value)
    }

    private fun setQuery(query: String) {
        Timber.tag(tag).d("save currentQuery = $query")
        savedStateHandle["query"] = query
    }

    fun restoreSavedQuery() {
        val query = savedStateHandle.get<String>("query")
        query?.let {
            _currentQuery.value = query
            _pendingScrollToTopAfterRefresh.value = true
            _isRefreshing.value = false
        }
        Timber.tag(tag).d("restore currentQuery = $query")
    }

    fun onFavoriteClick(wallpaper: Wallpaper) {
        val isFavorite = wallpaper.isFavorite
        wallpaper.isFavorite = !isFavorite
        onDispatcher(ioDispatcher) {
            wallpaperRepository.updateWallpaper(wallpaper)
        }
    }
}