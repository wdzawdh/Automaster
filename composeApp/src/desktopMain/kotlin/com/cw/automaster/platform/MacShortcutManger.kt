package com.cw.automaster.platform

import com.cw.automaster.permission.PermissionHelper
import com.cw.automaster.shortcut.JvmShortcutUtils
import com.cw.automaster.shortcut.MacShortcutUtils

object MacShortcutManger : ShortcutManager {
    override fun registerKeyEvent(global: Boolean, onKeyDown: (key: String) -> Boolean) {
        if (global && PermissionHelper.hasAccessibilityPermission()) {
            JvmShortcutUtils.unregisterShortcut()
            // 注册全局键盘监听器
            MacShortcutUtils.registerShortcut { key ->
                onKeyDown(key)
            }
        } else {
            MacShortcutUtils.unregisterShortcut()
            // 降级为Jvm实现，只能在应用前台使用
            JvmShortcutUtils.registerShortcut { key ->
                return@registerShortcut onKeyDown(key)
            }
        }
    }

    override fun unregisterKeyEvent(global: Boolean) {
        if (global) {
            MacShortcutUtils.unregisterShortcut()
        } else {
            JvmShortcutUtils.unregisterShortcut()
        }
    }
}