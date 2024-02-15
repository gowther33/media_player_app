package com.example.media_player_app.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    val id : Long,
    val name: String,
    val artist: String,
    val duration:Int,
    val contentUri: Uri
):Parcelable
