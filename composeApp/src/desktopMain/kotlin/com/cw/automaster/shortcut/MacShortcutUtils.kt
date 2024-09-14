package com.cw.automaster.shortcut

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.getPlatformType
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener

object MacShortcutUtils {

    private var nativeKeyListener: NativeKeyListener? = null

    private var hasInit = false

    private fun initHook() {
        if (hasInit) return
        GlobalScreen.registerNativeHook()
        hasInit = true
    }

    fun registerShortcut(onKeyPressed: (key: String) -> Unit) {
        initHook()
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            override fun nativeKeyPressed(nativeEvent: NativeKeyEvent) {
                val shortcut = getShortcut(nativeEvent)
                if (shortcut != null) {
                    onKeyPressed(shortcut)
                }
            }
        }.apply { nativeKeyListener = this })
    }

    fun unregisterShortcut() {
        GlobalScreen.removeNativeKeyListener(nativeKeyListener)
        nativeKeyListener = null
    }

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