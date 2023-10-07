package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.ui.theme.SwordDemoLibraryTheme


/*val LocalName = compositionLocalOf<String> {
  throw IllegalStateException("name 没有提供值")
}*/

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {

      /*CompositionLocalProvider(LocalName provides "sword") {
      }*/
    }
  }

  @Composable
  fun animateDpAsStateSample() {
    var big by remember { mutableStateOf(false) }
    val anim = animateDpAsState(
      if (big) 48.dp else 96.dp,
      spring(Spring.DampingRatioMediumBouncy),
      label = "")
    Box(
      Modifier
        .size(anim.value)
        .background(Color.Green)
        .clickable {
          big = !big
        }
    )
  }

  @Composable
  fun AnimatableSample() {
    var big by remember { mutableStateOf(false) }
    val targetValue = remember(big) { if (big) 96.dp else 48.dp }
    val animable = remember { Animatable(targetValue, Dp.VectorConverter) }

    //启动动画
    LaunchedEffect(targetValue) {
      animable.animateTo(targetValue, spring(Spring.DampingRatioMediumBouncy))
    }
    Box(
      Modifier
        .size(animable.value)
        .background(Color.Green)
        .clickable {
          big = !big
        }
    )
  }
}