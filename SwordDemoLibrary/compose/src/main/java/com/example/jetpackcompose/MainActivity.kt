package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.ui.theme.SwordDemoLibraryTheme


val LocalName = compositionLocalOf<String> {
  throw IllegalStateException("name 没有提供值")
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SwordDemoLibraryTheme {
        WeBottomBar()
      }

      LocalContext
      CompositionLocalProvider(LocalName provides "sword") {
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