package com.cw.automaster.dock

object WindowHelper {

    init {
        System.loadLibrary("window_helper")
    }

    external fun frontWindow()

    external fun setupDockListener()

    // 回调接口，用于将事件回调到 JVM
    private var onDockIconClicked: (() -> Unit)? = null

    fun setOnDockIconClicked(listener: () -> Unit) {
        onDockIconClicked = listener
        setupDockListener()
    }

    // 调用时触发回调
    fun triggerCallback() {
        onDockIconClicked?.invoke()
    }
}