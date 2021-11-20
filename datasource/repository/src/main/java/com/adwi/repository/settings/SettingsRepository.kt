package com.adwi.repository.settings

import com.adwi.domain.Duration
import com.adwi.domain.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>
    suspend fun insertSettings(settings: Settings)
    suspend fun updateLastQuery(query: String)
    suspend fun updatePushNotifications(enabled: Boolean)
    suspend fun updateNewWallpaperSet(enabled: Boolean)
    suspend fun updateWallpaperRecommendations(enabled: Boolean)
    suspend fun updateAutoChangeWallpaper(enabled: Boolean)
    suspend fun updateDownloadOverWiFi(enabled: Boolean)
    suspend fun updateChangeDurationSelected(durationSelected: Duration)
    suspend fun updateChangeDurationValue(durationValue: Float)
    suspend fun updateAutoHome(enabled: Boolean)
    suspend fun updateAutoLock(enabled: Boolean)
    suspend fun resetAllSettings()
}
