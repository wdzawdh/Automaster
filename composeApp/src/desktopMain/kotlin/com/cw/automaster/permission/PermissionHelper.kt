package com.cw.automaster.permission


object PermissionHelper {
    init {
        System.loadLibrary("permission_helper")
    }

    external fun hasAccessibilityPermission(): Boolean

    external fun openAccessibilitySettings()

    external fun addLoginItem()

    external fun removeLoginItem()

    fun toggleAutoLaunch(isAuto: Boolean) {
        if (isAuto) {
            addLoginItem()
        } else {
            removeLoginItem()
        }
    }
}