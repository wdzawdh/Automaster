package com.cw.automaster.platform

import com.cw.automaster.model.Shortcut
import com.cw.automaster.shortcut.ShortcutHelper

object MacShortcutManger : ShortcutManager {

    override fun setOnKeyDownListener(onKeyDown: (key: Shortcut) -> Unit) {
        ShortcutHelper.setOnShortcutPressed {
            onKeyDown(it)
        }
    }

    override fun registerKeyEvent(shortcut: Shortcut): Boolean {
        return ShortcutHelper.registerShortcut(shortcut)
    }

    override fun unregisterKeyEvent(shortcut: Shortcut) {
        ShortcutHelper.unregisterShortcut(shortcut)
    }
}