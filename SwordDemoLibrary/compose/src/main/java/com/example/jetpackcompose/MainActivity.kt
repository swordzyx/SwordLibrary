package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.ui.theme.SwordDemoLibraryTheme


/*val LocalName = compositionLocalOf<String> {
  throw IllegalStateException("name 没有提供值")
}*/

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var big by mutableStateOf(false)
    setContent {
      SwordDemoLibraryTheme {
        //WeBottomBar()
        val anim = animateDpAsState(if (big) 48.dp else 96.dp)
        Box(
          Modifier
            .size(anim.value)
            .background(Color.Green)
            .clickable {
              big = !big
            }
        )
      }
      /*CompositionLocalProvider(LocalName provides "sword") {
      }*/
    }
  }
}