package com.cw.automaster.permission


@Suppress("UnsafeDynamicallyLoadedCode")
object AccessibilityHelper {
    init {
        System.loadLibrary("accessibility_helper")
    }

    @JvmStatic
    external fun hasAccessibilityPermission(): Boolean

    @JvmStatic
    external fun openAccessibilitySettings()
}