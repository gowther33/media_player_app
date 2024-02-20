package com.example.media_player_app.network

interface Downloader {

    fun downloadFile(url:String, filename:String):Long
}