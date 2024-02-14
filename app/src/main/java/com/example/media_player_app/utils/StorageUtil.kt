package com.example.media_player_app.utils

import android.os.Build

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}

inline fun sdk32AndDown() : Boolean{
    return Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2
}