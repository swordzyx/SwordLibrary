package com.zero.kotlinprimer.chapter4.unit6

sealed class PlayerViewType {
    object BLUE: PlayerViewType()
    object GREEN: PlayerViewType()
}

fun getPlayerView(type: PlayerViewType): PlayerView = when(type){
    PlayerViewType.BLUE -> BluePlayerView()
    PlayerViewType.GREEN -> GreenPlayerView()
}