package com.zero.kotlinprimer.chapter4.unit6


class PlayUI() {
    companion object {
        fun get(): PlayUI {
            return Holder.instance
        }
    }

    //匿名内部类
    private object Holder{
        val instance = PlayUI()
    }

    fun showUI(user: User){
        MediaPlayerView(getPlayerView(user.playerType)).showView()
    }
}