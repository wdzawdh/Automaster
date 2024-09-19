package com.cw.automaster

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.cw.automaster.dock.DockListener
import com.cw.automaster.platform.MacWorkflowManager
import com.cw.automaster.tray.TrayManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Dimension


fun main() = application {
    val isVisible = remember { mutableStateOf(true) }

    // Tray
    MacWorkflowManager.initTray()

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
    }
}

private fun setDockListener(onDockIconClicked: (() -> Unit)) {
    GlobalScope.launch {
        val dockListener = DockListener()
        dockListener.onDockIconClicked = onDockIconClicked
        dockListener.setupDockListener()
    }
}

