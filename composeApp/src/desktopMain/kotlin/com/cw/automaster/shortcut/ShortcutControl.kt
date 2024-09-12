package com.cw.automaster.shortcut

import SHORTCUT_DIALOG_NAME
import com.cw.automaster.manager.ConfigManager
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.LoadingManager
import com.cw.automaster.permission.AccessibilityHelper
import com.cw.automaster.platform.MacWorkflowManager
import com.cw.automaster.utils.JvmShortcutUtils
import com.github.kwhat.jnativehook.GlobalScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun initMacShortcut() {
    if (AccessibilityHelper.hasAccessibilityPermission()) {
        // 注册全局键盘监听器
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(object : MacShortcutListener() {
            override fun onKeyPressed(key: String) {
                if (!DialogManager.isShow(SHORTCUT_DIALOG_NAME)) { //判断快捷键非Dialog弹出
                    ConfigManager.getConfigs().forEach {
                        if (it.shortcut == key) {
                            GlobalScope.launch {
                                LoadingManager.loading()
                                MacWorkflowManager.runWorkflow(it.path)
                                LoadingManager.dismiss()
                            }
                        }
                    }
                }
            }
        })
        JvmShortcutUtils.unregisterShortcut()
    } else {
        // 降级为Jvm实现，只能在应用前台使用
        JvmShortcutUtils.registerShortcut { key ->
            if (!DialogManager.isShow(SHORTCUT_DIALOG_NAME)) {
                ConfigManager.getConfigs().forEach {
                    if (it.shortcut == key) {
                        GlobalScope.launch {
                            LoadingManager.loading()
                            MacWorkflowManager.runWorkflow(it.path)
                            LoadingManager.dismiss()
                        }
                    }
                }
            }
            return@registerShortcut false
        }
    }
}