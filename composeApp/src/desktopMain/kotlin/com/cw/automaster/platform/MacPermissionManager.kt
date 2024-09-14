package com.cw.automaster.platform

import MessageDialog
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.permission.AccessibilityHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object MacPermissionManager : PermissionManager {

    override fun checkPermission(): Boolean {
        return AccessibilityHelper.hasAccessibilityPermission()
    }

    override fun requestPermission(callback: (success: Boolean) -> Unit) {
        AccessibilityHelper.openAccessibilitySettings()
        runBlocking { delay(2000) }
        DialogManager.show {
            MessageDialog(
                message = "添加“辅助功能”权限后需要刷新配置才能生效，是否刷新?",
                confirmText = "刷新"
            ) { confirm ->
                DialogManager.dismiss()
                if (confirm && checkPermission()) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
        }
    }
}