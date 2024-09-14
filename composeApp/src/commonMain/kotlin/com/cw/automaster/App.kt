package com.cw.automaster

import SHORTCUT_DIALOG_NAME
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.cw.automaster.manager.ConfigManager
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.LoadingManager
import com.cw.automaster.manager.Screen
import com.cw.automaster.manager.ScreenManager.CurrentScreen
import com.cw.automaster.manager.SnackbarManager
import com.cw.automaster.widget.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// global object
val platformType = getPlatformType()
val workflowManager = getWorkflowManager()
val fileSelector = getFileSelector()
val permissionManager = getPermissionManager()
val shortcutManager = getShortcutManager()
val keyValueStore = getKeyValueStore()

@Composable
fun App() {
    // config
    ConfigManager.init()

    // shortcut & permission
    checkPermission(rememberCoroutineScope())
    registerKeyboard(keyValueStore?.getBoolean(KEY_GLOBAL_SHORTCUT) == true)

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

private fun checkPermission(scope: CoroutineScope) {
    if (permissionManager?.checkPermission() != true) {
        SnackbarManager.showMessage(
            coroutineScope = scope,
            message = "打开“辅助功能”可以启用全局快捷键",
            actionLabel = "去打开"
        ) {
            permissionManager?.requestPermission {
                if (it) {
                    registerKeyboard(true)
                    SnackbarManager.showMessage(scope, "全局快捷键已打开")
                }
            }
        }
        keyValueStore?.setBoolean(KEY_GLOBAL_SHORTCUT, false)
    }
}

fun registerKeyboard(global: Boolean) {
    shortcutManager?.registerKeyEvent(global) { key ->
        if (!DialogManager.isShow(SHORTCUT_DIALOG_NAME)) {
            ConfigManager.getConfigs().forEach {
                if (it.shortcut == key) {
                    GlobalScope.launch {
                        LoadingManager.loading()
                        workflowManager?.runWorkflow(it.path)
                        LoadingManager.dismiss()
                    }
                    return@registerKeyEvent true
                }
            }
        }
        return@registerKeyEvent false
    }
    keyValueStore?.setBoolean(KEY_GLOBAL_SHORTCUT, global)
}

