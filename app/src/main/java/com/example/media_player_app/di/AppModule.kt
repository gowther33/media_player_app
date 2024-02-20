package com.example.media_player_app.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.media_player_app.mediaPlayer.MusicPlayer
import com.example.media_player_app.network.ConnectivityObserver
import com.example.media_player_app.network.NetworkConnectivityObserver
import com.example.media_player_app.repository.DefaultSongsRepository
import com.example.media_player_app.repository.SongsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesExoPlayer(
        @ApplicationContext context: Context
    ):ExoPlayer = MusicPlayer.getInstance(context)


    @Singleton
    @Provides
    fun providesSongsRepository(
        @ApplicationContext context: Context
    ):SongsRepository = DefaultSongsRepository(context)

    @Singleton
    @Provides
    fun providesConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver = NetworkConnectivityObserver(context)
}