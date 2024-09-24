package com.cw.automaster.permission


object AccessibilityHelper {
    init {
        System.loadLibrary("accessibility_helper")
    }

    external fun hasAccessibilityPermission(): Boolean

    external fun openAccessibilitySettings()
}