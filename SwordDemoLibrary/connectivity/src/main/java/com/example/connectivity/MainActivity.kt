package com.example.connectivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.connectivity.util.NetworkUtilK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val networkUtil = NetworkUtilK()
        networkUtil.readDefaultNetworkState(this)
    }
}