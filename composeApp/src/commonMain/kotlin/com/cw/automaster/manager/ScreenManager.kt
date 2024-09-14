package com.cw.automaster.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.cw.automaster.screen.MainScreen
import com.cw.automaster.screen.ToolScreen

enum class Screen {
    MAIN,
    SETTING
}

object ScreenManager {
    private var currentScreen: MutableState<Screen>? = null

    @Composable
    fun CurrentScreen(currentScreen: MutableState<Screen>) {
        this.currentScreen = currentScreen
        when (currentScreen.value) {
            Screen.MAIN -> {
                MainScreen()
            }

            Screen.SETTING -> {
                ToolScreen()
            }
        }
    }

    fun openScreen(screen: Screen) {
        currentScreen?.value = screen
    }
}