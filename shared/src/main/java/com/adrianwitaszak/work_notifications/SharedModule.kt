package com.adrianwitaszak.work_notifications

import android.app.WallpaperManager
import android.content.Context
import androidx.work.WorkManager
import com.adrianwitaszak.work_notifications.image.ImageTools
import com.adrianwitaszak.work_notifications.notifications.NotificationTools
import com.adrianwitaszak.work_notifications.setter.WallpaperSetter
import com.adrianwitaszak.work_notifications.sharing.SharingTools
import com.adrianwitaszak.work_notifications.work.WorkTools
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Singleton
    @Provides
    fun provideWorkTools(
        workManager: WorkManager
    ) = WorkTools(workManager)

    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideWallpaperManager(
        @ApplicationContext context: Context
    ): WallpaperManager = WallpaperManager.getInstance(context)

    @Provides
    @Singleton
    fun provideWallpaperTools(
        @ApplicationContext context: Context,
        wallpaperManager: WallpaperManager
    ): WallpaperSetter = WallpaperSetter(context, wallpaperManager)

    @Provides
    @Singleton
    fun provideSharingTools(
        @ApplicationContext context: Context
    ): SharingTools = SharingTools(context)

    @Provides
    @Singleton
    fun provideNotificationTools(
        @ApplicationContext context: Context
    ): NotificationTools = NotificationTools(context)

    @Provides
    @Singleton
    fun provideImageTools(
        @ApplicationContext context: Context
    ): ImageTools = ImageTools(context)
}