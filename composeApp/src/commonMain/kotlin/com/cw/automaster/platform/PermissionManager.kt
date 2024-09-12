package com.cw.automaster.platform

interface PermissionManager {
    fun checkPermission(): Boolean
    fun requestPermission(callback: (success: Boolean) -> Unit)
}