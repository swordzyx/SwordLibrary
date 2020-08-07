package com.zero.kotlinprimer.chapter4.unit6

import javax.swing.JOptionPane

class GreenPlayerView : PlayerView {
    override fun getPlayButton() {
        println("绿色播放器")
    }

    override fun showView() {
        JOptionPane.showConfirmDialog(null, "显示绿色播放器", "绿色播放器", JOptionPane.DEFAULT_OPTION)
    }
}