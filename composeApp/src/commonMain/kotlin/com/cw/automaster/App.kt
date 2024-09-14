package com.cw.automaster

import SHORTCUT_DIALOG_NAME
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.cw.automaster.emum.PlatformType
import com.cw.automaster.manager.ConfigManager
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.LoadingManager
import com.cw.automaster.manager.Screen
import com.cw.automaster.manager.ScreenManager.CurrentScreen
import com.cw.automaster.manager.SnackbarManager
import com.cw.automaster.widget.Loading
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

    // shortcut
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

    // mac shortcut permission
    checkMacPermission()
}


@Composable
private fun checkMacPermission() {
    if (platformType == PlatformType.MAC && permissionManager?.checkPermission() != true) {
        val scope = rememberCoroutineScope()
        SnackbarManager.showMessage(
            coroutineScope = scope,
            message = "打开“辅助功能”可以使用全局快捷键",
            actionLabel = "去打开"
        ) {
            permissionManager?.requestPermission {
                registerKeyboard(true)
                SnackbarManager.showMessage(
                    scope,
                    if (it) "全局快捷键已打开" else "打开失败"
                )
            }
        }
    }
}

fun registerKeyboard(global: Boolean) {
    shortcutManager?.registerKeyEvent(global) { key ->
        if (!DialogManager.isShow(SHORTCUT_DIALOG_NAME)) { //判断快捷键非Dialog弹出
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

