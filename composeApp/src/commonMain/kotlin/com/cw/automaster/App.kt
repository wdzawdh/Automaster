package com.cw.automaster

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.cw.automaster.manager.ConfigManager
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.LoadingManager
import com.cw.automaster.manager.Screen
import com.cw.automaster.manager.ScreenManager.CurrentScreen
import com.cw.automaster.manager.SnackbarManager
import com.cw.automaster.widget.Loading

// global object
val platformType = getPlatformType()
val workflowManager = getWorkflowManager()
val fileSelector = getFileSelector()
val permissionManager = getPermissionManager()

@Composable
fun App() {
    // config
    ConfigManager.init()

    Scaffold(
        snackbarHost = {
            // snack bar
            SnackbarHost(SnackbarManager.getSnackbarHostState())
        },
        content = {
            // screen
            CurrentScreen(remember { mutableStateOf(Screen.MAIN) })
            // dialog
            val dialogState = remember { mutableStateOf(false) }
            DialogManager.getContent(dialogState)?.invoke()
            // loading
            val loadingState = remember { mutableStateOf(false) }
            LoadingManager.getContent(loadingState)?.invoke()
            LoadingManager.setContent { Loading() }
        }
    )
}

