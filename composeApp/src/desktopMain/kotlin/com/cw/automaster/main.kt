package com.cw.automaster

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.cw.automaster.dock.DockListener
import com.cw.automaster.manager.SnackbarManager
import com.cw.automaster.platform.MacPermissionManager
import com.cw.automaster.tray.TrayManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Dimension


fun main() = application {
    val isVisible = remember { mutableStateOf(true) }

    // Tray
    TrayManager.initTray()

    val windowState = rememberWindowState(
        width = 450.dp,  // 设置窗口的默认宽度
        height = 800.dp  // 设置窗口的默认高度
    )
    Window(
        onCloseRequest = { isVisible.value = false },
        visible = isVisible.value,
        state = windowState,  // 使用窗口状态来管理窗口的大小
        title = "Automaster"
    ) {
        // 窗口最小宽高
        window.minimumSize = Dimension(400, 400)
        // 监听Dock图标被点击
        setDockListener {
            isVisible.value = true
        }
        // UI
        App()
        // 快捷键
        checkMacShortcut()
    }
}

@Composable
private fun checkMacShortcut() {
    if (!MacPermissionManager.checkPermission()) {
        val scope = rememberCoroutineScope()
        SnackbarManager.showMessage(
            coroutineScope = scope,
            message = "打开“辅助功能”可以使用全局快捷键",
            actionLabel = "去打开"
        ) {
            MacPermissionManager.requestPermission {
                SnackbarManager.showMessage(
                    scope,
                    if (it) "全局快捷键已打开" else "打开失败"
                )
            }
        }
    }
}

private fun setDockListener(onDockIconClicked: (() -> Unit)) {
    GlobalScope.launch {
        val dockListener = DockListener()
        dockListener.onDockIconClicked = onDockIconClicked
        dockListener.setupDockListener()
    }
}

