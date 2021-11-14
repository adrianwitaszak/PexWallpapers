package com.adwi.components.domain

import com.adwi.core.domain.ProgressBarState
import com.adwi.domain.Wallpaper

data class WallpaperState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val wallpaper: Wallpaper = Wallpaper.defaultDaily
)