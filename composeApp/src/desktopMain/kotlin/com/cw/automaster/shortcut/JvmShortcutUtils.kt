package com.cw.automaster.shortcut

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.getPlatformType
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

object JvmShortcutUtils {

    private var dispatcher: KeyEventDispatcher? = null

    /**
     * 注册快捷键监听
     */
    fun registerShortcut(onKeyPressed: (key: String) -> Boolean) {
        if (dispatcher != null) return
        val manager = KeyboardFocusManager.getCurrentKeyboardFocusManager()
        manager.addKeyEventDispatcher(object : KeyEventDispatcher {
            override fun dispatchKeyEvent(e: KeyEvent): Boolean {
                if (e.id == KeyEvent.KEY_PRESSED) {
                    val shortcutKey = getShortcut(e)
                    if (shortcutKey != null) {
                        return onKeyPressed(shortcutKey)
                    }
                }
                return false
            }
        }.apply { dispatcher = this })
    }

    fun unregisterShortcut() {
        if (dispatcher != null) {
            val manager = KeyboardFocusManager.getCurrentKeyboardFocusManager()
            manager.removeKeyEventDispatcher(dispatcher)
            dispatcher = null
        }
    }

    private fun getShortcut(keyEvent: KeyEvent): String? {
        val keyText = KeyEvent.getKeyText(keyEvent.keyCode)
        if (keyText in keys) {
            val platformType = getPlatformType()
            if (platformType == PlatformType.MAC) {
                return macShortcut(keyEvent)
            }
        }
        return null
    }

    private fun macShortcut(keyEvent: KeyEvent): String? {
        if (keyEvent.isControlDown || keyEvent.isShiftDown || keyEvent.isMetaDown || keyEvent.isAltDown) {
            return buildString {
                if (keyEvent.isControlDown) {
                    append("⌃+")
                }
                if (keyEvent.isShiftDown) {
                    append("⇧+")
                }
                if (keyEvent.isMetaDown) {
                    append("⌘+")
                }
                if (keyEvent.isAltDown) {
                    append("⌥+")
                }
                append(KeyEvent.getKeyText(keyEvent.keyCode))
            }
        }
        return null
    }

    private val keys = listOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z",
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
    )
}