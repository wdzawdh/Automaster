package com.cw.automaster.platform

interface ShortcutManager {
    fun registerKeyEvent(global: Boolean, onKeyDown: (key: String) -> Boolean)
    fun unregisterKeyEvent(global: Boolean)
}