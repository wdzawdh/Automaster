package com.cw.automaster

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.cw.automaster.dock.WindowHelper
import com.cw.automaster.platform.MacWorkflowManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.awt.Dimension

val windowVisible = mutableStateOf(true)

fun main() = application {

    // Tray
    MacWorkflowManager.initTray()

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
        // UI
        App()
    }
}

private fun setDockListener(onDockIconClicked: (() -> Unit)) {
    GlobalScope.launch {
        WindowHelper.setOnDockIconClicked(onDockIconClicked)
    }
}

