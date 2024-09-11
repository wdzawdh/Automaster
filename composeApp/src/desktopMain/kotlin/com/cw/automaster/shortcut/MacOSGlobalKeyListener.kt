package com.cw.automaster.shortcut

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.getPlatformType
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener

abstract class MacOSGlobalKeyListener : NativeKeyListener {

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        // 在这里处理按键事件
        val shortcut = getShortcut(e)
        if (shortcut != null) {
            onKeyPressed(shortcut)
        }
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        // 可选：处理按键释放事件
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
        // 可选：处理键入事件
    }

    abstract fun onKeyPressed(key: String)

    private fun getShortcut(keyEvent: NativeKeyEvent): String? {
        val keyText = NativeKeyEvent.getKeyText(keyEvent.keyCode)
        if (keyText in keys) {
            val platformType = getPlatformType()
            if (platformType == PlatformType.MAC) {
                return macShortcut(keyEvent)
            }
        }
        return null
    }

    private fun macShortcut(keyEvent: NativeKeyEvent): String? {
        if (keyEvent.isControlDown() || keyEvent.isShiftDown() || keyEvent.isMetaDown() || keyEvent.isAltDown()) {
            return buildString {
                if (keyEvent.isControlDown()) {
                    append("⌃+")
                }
                if (keyEvent.isShiftDown()) {
                    append("⇧+")
                }
                if (keyEvent.isMetaDown()) {
                    append("⌘+")
                }
                if (keyEvent.isAltDown()) {
                    append("⌥+")
                }
                append(NativeKeyEvent.getKeyText(keyEvent.keyCode))
            }
        }
        return null
    }

    private fun NativeKeyEvent.isControlDown(): Boolean {
        return modifiers and NativeKeyEvent.CTRL_MASK != 0
    }

    private fun NativeKeyEvent.isShiftDown(): Boolean {
        return modifiers and NativeKeyEvent.SHIFT_MASK != 0
    }

    private fun NativeKeyEvent.isMetaDown(): Boolean {
        return modifiers and NativeKeyEvent.META_MASK != 0
    }

    private fun NativeKeyEvent.isAltDown(): Boolean {
        return modifiers and NativeKeyEvent.ALT_MASK != 0
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