package com.cw.automaster.dock

import java.nio.file.Paths

class DockListener {

    external fun setupDockListener()

    // 加载本地库
    @Suppress("UnsafeDynamicallyLoadedCode")
    companion object {
        init {
            System.load(
                Paths.get("src/desktopMain/libs/libdocklistener.dylib").toAbsolutePath().toString()
            )
        }
    }

    // 回调接口，用于将事件回调到 JVM
    var onDockIconClicked: (() -> Unit)? = null

    // 调用时触发回调
    fun triggerCallback() {
        onDockIconClicked?.invoke()
    }
}