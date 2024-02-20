package com.example.media_player_app.network

import com.example.media_player_app.utils.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<ConnectivityStatus>
}