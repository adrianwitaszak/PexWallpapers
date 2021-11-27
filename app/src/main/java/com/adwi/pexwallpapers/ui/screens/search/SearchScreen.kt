package com.adwi.pexwallpapers.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.adwi.pexwallpapers.data.wallpapers.database.domain.WallpaperEntity
import com.adwi.pexwallpapers.data.wallpapers.database.domain.toDomain
import com.adwi.pexwallpapers.model.Wallpaper
import com.adwi.pexwallpapers.ui.components.*
import com.adwi.pexwallpapers.ui.theme.paddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@ExperimentalPagingApi
@ExperimentalFoundationApi
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onWallpaperClick: (Int) -> Unit
) {
    val wallpapers = viewModel.searchResults.collectAsLazyPagingItems()
    val currentQuery by viewModel.currentQuery.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pendingScrollToTopAfterRefresh by viewModel.pendingScrollToTopAfterRefresh.collectAsState()

    if (wallpapers.loadState.refresh == LoadState.Loading) {
        viewModel.setIsRefreshing(true)
    }

    val listState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(pendingScrollToTopAfterRefresh && wallpapers.loadState.refresh != LoadState.Loading) {
        listState.animateScrollToItem(0)
        viewModel.setPendingScrollToTopAfterRefresh(false)
    }

    PexScaffold(
        viewModel = viewModel,
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val swipeRefreshState =
                rememberSwipeRefreshState(isRefreshing && wallpapers.itemCount == 0)

            PexSearchToolbar(
                query = currentQuery,
                onQueryChanged = { viewModel.onSearchQuerySubmit(it) },
                onShowFilterDialog = {},
                modifier = Modifier
                    .padding(horizontal = paddingValues)
                    .padding(vertical = paddingValues)
                    .fillMaxWidth()
            )
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onSearchQuerySubmit(currentQuery) },
            ) {
                AnimatedVisibility(
                    visible = wallpapers.itemCount > 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    WallpaperListPaged(
                        modifier = Modifier.fillMaxSize(),
                        wallpapers = wallpapers,
                        onWallpaperClick = onWallpaperClick,
                        onLongPress = { viewModel.onFavoriteClick(it) }
                    )
                }
            }
            NothingHereYetMessage(visible = currentQuery.isEmpty())
        }
    }
}

@Composable
private fun NothingHereYetMessage(
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nothing here yet",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = "Press \"Search bar\" to start new search",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(paddingValues / 2)
                )
                Text(
                    text = "Shall we?",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun WallpaperListPaged(
    modifier: Modifier = Modifier,
    wallpapers: LazyPagingItems<WallpaperEntity>,
    onWallpaperClick: (Int) -> Unit,
    onLongPress: (Wallpaper) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(
            top = paddingValues,
            bottom = paddingValues * 3
        ),
        verticalArrangement = Arrangement.spacedBy(paddingValues / 2)
    ) {
        items(wallpapers.itemCount) { index ->
            wallpapers[index]?.let {
                val wallpaper = it.toDomain()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = paddingValues)
                        .height((wallpaper.height / 2.5).dp)
                        .neumorphicShadow(pressed = isPressed)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onWallpaperClick(wallpaper.id) },
                                onLongPress = {
                                    onLongPress(wallpaper)
                                }
                            )
                        },
//                    elevation = 20.dp,
                    shape = MaterialTheme.shapes.large
                ) {
                    Box() {
                        PexCoilImage(
                            imageUrl = wallpaper.imageUrlPortrait,
                            modifier = Modifier.fillMaxSize()
                        )
                        PexAnimatedHeart(
                            state = wallpaper.isFavorite,
                            size = 64.dp,
                            speed = 1.5f,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }
    }
}