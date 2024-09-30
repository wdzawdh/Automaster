package com.cw.automaster.platform

import com.cw.automaster.model.Shortcut

interface ShortcutManager {
    fun setOnKeyDownListener(onKeyDown: (shortcut: Shortcut) -> Unit)
    fun registerKeyEvent(shortcut: Shortcut): Boolean
    fun unregisterKeyEvent(shortcut: Shortcut)
}