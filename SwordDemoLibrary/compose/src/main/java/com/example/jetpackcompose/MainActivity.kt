package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.KeyframesSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
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


/*val LocalName = compositionLocalOf<String> {
  throw IllegalStateException("name 没有提供值")
}*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            /*CompositionLocalProvider(LocalName provides "sword") {
            }*/
            SpringSpecSample()
        }
    }

    @Composable
    fun animateDpAsStateSample() {
        var big by remember { mutableStateOf(false) }
        val anim = animateDpAsState(
            if (big) 48.dp else 96.dp,
            spring(Spring.DampingRatioMediumBouncy),
            label = ""
        )
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
    fun RepeatableSpecSample() {
        var start by remember { mutableStateOf(false) }
        val targetValue = remember(start) { if (start) 0.dp else 200.dp }
        val animable = remember { Animatable(targetValue, Dp.VectorConverter) }

        //启动动画
        LaunchedEffect(targetValue) {
            animable.animateTo(targetValue, repeatable(3, tween(), RepeatMode.Reverse, StartOffset(500)))
        }
    }

    @Composable
    fun AnimatableSample() {
        var start by remember { mutableStateOf(false) }
        val targetValue = remember(start) { if (start) 0.dp else 200.dp }
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
                    start = !start
                }
        )
    }

    @Composable
    fun TweenSpecSample() {
        var big by remember { mutableStateOf(false) }
        val targetValue = remember(big) { if (big) 96.dp else 48.dp }
        val animable = remember { Animatable(targetValue, Dp.VectorConverter) }

        //启动动画
        LaunchedEffect(targetValue) {
            animable.animateTo(targetValue, TweenSpec(easing = FastOutSlowInEasing))
        }
        Box(
            Modifier
                .size(48.dp)
                .offset(animable.value, animable.value)
                .background(Color.Green)
                .clickable {
                    big = !big
                }
        )
    }

    @Composable
    fun KeyframesSpecSample() {
        var start by remember { mutableStateOf(false) }
        val targetValue = remember(start) { if (start) 0.dp else 200.dp }
        val animable = remember { Animatable(targetValue, Dp.VectorConverter) }

        //启动动画
        LaunchedEffect(targetValue) {
            animable.animateTo(
                targetValue,
                keyframes {
                    durationMillis = 450 //设置动画的时长
                    delayMillis = 500 //设置延时
                    144.dp at 150 with FastOutSlowInEasing //设置 150ms - 300ms 之间速度曲线
                    20.dp at 300
                }
            )
        }
        Box(
            Modifier
                .size(animable.value)
                .background(Color.Green)
                .clickable {
                    start = !start
                }
        )
    }

    @Composable
    fun SpringSpecSample() {
        var big by remember { mutableStateOf(false) }
        val targetValue = remember(big) { if (big) 96.dp else 48.dp }
        val animable = remember { Animatable(targetValue, Dp.VectorConverter) }

        LaunchedEffect(key1 = targetValue) {
            animable.animateTo(targetValue, SpringSpec(Spring.DampingRatioNoBouncy))
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