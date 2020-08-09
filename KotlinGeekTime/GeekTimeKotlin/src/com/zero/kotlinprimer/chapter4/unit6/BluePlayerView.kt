package com.zero.kotlinprimer.chapter4.unit6

import javax.swing.JOptionPane

class BluePlayerView : PlayerView {
    override fun getPlayButton() {
        println("显示蓝色按钮")
    }

    override fun showView() {
        JOptionPane.showConfirmDialog(null, "显示蓝色播放器", "蓝色播放器", JOptionPane.DEFAULT_OPTION)
    }

}