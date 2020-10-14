package com.zero.kotlinprimer.chapter4.unit6

interface PlayerView  {
    fun getPlayButton()
    fun showView()
}

class MediaPlayerView(playerView: PlayerView): PlayerView by playerView