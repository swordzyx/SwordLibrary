package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.jetpackcompose.ui.theme.SwordDemoLibraryTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SwordDemoLibraryTheme {
        WeBottomBar()
      }
    }
  }

  @Composable
  private fun WeBottomBar() {
    Column {
      Icon(painter = painterResource(id = R.drawable.ic_me_filled), contentDescription = "我")
      Text(text = "")
    }
    
    
  }
}