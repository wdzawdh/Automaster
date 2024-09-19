package com.cw.automaster.permission


@Suppress("UnsafeDynamicallyLoadedCode")
object AccessibilityHelper {
    init {
        System.loadLibrary("accessibilityhelper")
    }

    @JvmStatic
    external fun hasAccessibilityPermission(): Boolean

    @JvmStatic
    external fun openAccessibilitySettings()
}