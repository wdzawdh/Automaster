package com.cw.automaster.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cw.automaster.theme.SecondaryColor

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 圆形背景和 Loading 圆圈
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16))
                .background(Color.LightGray.copy(alpha = 0.2f)), // 灰色半透明背景
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = SecondaryColor,
                strokeWidth = 3.dp
            )
        }
    }
}