package com.cw.automaster.platform

import MessageDialog
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.permission.AccessibilityHelper
import com.cw.automaster.shortcut.initMacShortcut

object MacPermissionManager : PermissionManager {

    override fun checkPermission(): Boolean {
        return AccessibilityHelper.hasAccessibilityPermission()
    }

    override fun requestPermission(callback: (success: Boolean) -> Unit) {
        DialogManager.show {
            MessageDialog(
                message = "跳转系统设置添加“辅助功能”权限"
            ) { confirm ->
                DialogManager.dismiss()
                if (confirm) {
                    AccessibilityHelper.openAccessibilitySettings()
                    showRelaunchDialog(callback)
                } else {
                    callback(false)
                }
            }
        }
    }

    private fun showRelaunchDialog(callback: (success: Boolean) -> Unit) {
        DialogManager.show {
            MessageDialog(
                message = "添加“辅助功能”权限后需要刷新才能生效，是否刷新?"
            ) { confirm ->
                DialogManager.dismiss()
                if (confirm && checkPermission()) {
                    initMacShortcut()
                    callback(true)
                } else {
                    callback(false)
                }
            }
        }
    }
}