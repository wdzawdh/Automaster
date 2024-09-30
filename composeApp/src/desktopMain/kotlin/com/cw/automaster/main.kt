package com.cw.automaster

import SHORTCUT_DIALOG_NAME
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.cw.automaster.dock.WindowHelper
import com.cw.automaster.manager.ConfigManager
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.LoadingManager
import com.cw.automaster.model.Setting
import com.cw.automaster.permission.PermissionHelper
import com.cw.automaster.platform.MacShortcutManger
import com.cw.automaster.platform.MacWorkflowManager
import com.cw.automaster.shortcut.ShortcutHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Dimension

val windowVisible = mutableStateOf(true)

fun main() = application {
    // Tray
    MacWorkflowManager.initTray()

    // dock
    val windowVisibleDer = derivedStateOf { windowVisible.value }
    LaunchedEffect(windowVisibleDer.value) {
        WindowHelper.toggleDockIcon(
            windowVisibleDer.value || keyValueStore?.getBoolean(KEY_HIDE_DOCK_ICON) == false
        )
    }

    val windowState = rememberWindowState(
        width = 450.dp,  // 设置窗口的默认宽度
        height = 800.dp  // 设置窗口的默认高度
    )
    Window(
        onCloseRequest = { windowVisible.value = false },
        visible = windowVisible.value,
        state = windowState,  // 使用窗口状态来管理窗口的大小
        title = "Automaster"
    ) {
        // 窗口最小宽高
        window.minimumSize = Dimension(400, 400)
        // 监听Dock图标被点击
        setDockListener {
            windowVisible.value = true
        }
        // 设置项
        addSettingItems()
        // UI
        App()
        // shortcut
        registerKeyboard()
    }
}

private fun addSettingItems() {
    settingItems.clear()
    val autoLaunch = mutableStateOf(keyValueStore?.getBoolean(KEY_AUTO_LAUNCH) == true)
    Setting("开机自启动", autoLaunch) { isAuto ->
        PermissionHelper.toggleAutoLaunch(isAuto)
        keyValueStore?.setBoolean(KEY_AUTO_LAUNCH, isAuto)
        autoLaunch.value = isAuto
    }.apply { settingItems.add(this) }
    val hideDockKey = mutableStateOf(keyValueStore?.getBoolean(KEY_HIDE_DOCK_ICON) == true)
    Setting("后台运行时隐藏程序坞图标", hideDockKey) { isHide ->
        keyValueStore?.setBoolean(KEY_HIDE_DOCK_ICON, isHide)
        hideDockKey.value = isHide
    }.apply { settingItems.add(this) }
}

private fun setDockListener(onDockIconClicked: (() -> Unit)) {
    GlobalScope.launch {
        WindowHelper.setOnDockIconClicked(onDockIconClicked)
    }
}

fun registerKeyboard() {
    MacShortcutManger.setOnKeyDownListener { key ->
        if (!DialogManager.isShow(SHORTCUT_DIALOG_NAME)) {
            ConfigManager.getConfigs().forEach {
                if (it.shortcut == key) {
                    GlobalScope.launch {
                        LoadingManager.loading()
                        workflowManager?.runWorkflow(it.path)
                        LoadingManager.dismiss()
                    }
                    return@setOnKeyDownListener
                }
            }
        }
    }
    ConfigManager.getConfigs().forEach {
        val shortcut = it.shortcut
        if (shortcut != null) {
            MacShortcutManger.registerKeyEvent(shortcut)
        }
    }
}

