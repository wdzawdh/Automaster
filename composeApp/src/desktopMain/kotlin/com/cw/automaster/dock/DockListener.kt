package com.cw.automaster.dock

class DockListener {

    external fun setupDockListener()

    // 加载本地库
    @Suppress("UnsafeDynamicallyLoadedCode")
    companion object {
        init {
            System.loadLibrary("docklistener")
        }
    }

    // 回调接口，用于将事件回调到 JVM
    var onDockIconClicked: (() -> Unit)? = null

    // 调用时触发回调
    fun triggerCallback() {
        onDockIconClicked?.invoke()
    }
}