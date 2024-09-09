package com.cw.automaster.dock

object DockListener {

    private var listener: (() -> Unit)? = null

    // 加载本地库
    init {
        System.load("/Users/caowei/Documents/KmmProject/Automaster/composeApp/libs/libdocklistener.dylib")
    }

    // 声明 JNI 方法
    @JvmStatic
    external fun setupDockListener()

    // Java 的回调方法，当 Dock 图标被点击时调用
    @JvmStatic
    fun onDockIconClick() {
        println("Dock icon was clicked!")
        listener?.invoke()
    }

    fun setOnDockIconClickListener(listener: () -> Unit) {
        //setupDockListener()
        this.listener = listener
    }
}