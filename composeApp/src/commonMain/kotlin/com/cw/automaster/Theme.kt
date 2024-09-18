package com.cw.automaster

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.cw.automaster.theme.ThemeColor
import com.cw.automaster.theme.SecondaryColor

private val LightColorPalette = lightColors(
    primary = ThemeColor,
    primaryVariant = ThemeColor,
    secondary = SecondaryColor
)

private val DarkColorPalette = darkColors(
    primary = ThemeColor,
    primaryVariant = ThemeColor,
    secondary = SecondaryColor
)

// 自定义形状
private val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)

// 自定义排版
private val Typography = Typography()

// 定义主题
@Composable
fun Theme(
    darkTheme: Boolean = false, // 设置是否启用深色模式
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}