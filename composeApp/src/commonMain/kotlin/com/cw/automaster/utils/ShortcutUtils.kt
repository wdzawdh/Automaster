package com.cw.automaster.utils

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.cw.automaster.emum.PlatformType
import com.cw.automaster.getPlatformType
import com.cw.automaster.model.Shortcut

object ShortcutUtils {

    fun getShortcut(keyEvent: KeyEvent): Shortcut? {
        if (keyEvent.key in keys && keyEvent.type == KeyEventType.KeyDown) {
            val platformType = getPlatformType()
            if (platformType == PlatformType.MAC) {
                return macShortcut(keyEvent)
            }
        }
        return null
    }

    private fun macShortcut(keyEvent: KeyEvent): Shortcut? {
        if (keyEvent.isCtrlPressed || keyEvent.isShiftPressed || keyEvent.isMetaPressed || keyEvent.isAltPressed) {
            return Shortcut(
                key = keyEvent.key.toString().replace("Key: ", ""),
                modifiers = arrayListOf<String>().apply {
                    if (keyEvent.isMetaPressed) {
                        add("⌘")
                    }
                    if (keyEvent.isShiftPressed) {
                        add("⇧")
                    }
                    if (keyEvent.isCtrlPressed) {
                        add("⌃")
                    }
                    if (keyEvent.isAltPressed) {
                        add("⌥")
                    }
                }
            )
        }
        return null
    }

    private val keys = listOf(
        Key.A,
        Key.B,
        Key.C,
        Key.D,
        Key.E,
        Key.F,
        Key.G,
        Key.H,
        Key.I,
        Key.J,
        Key.K,
        Key.L,
        Key.M,
        Key.N,
        Key.O,
        Key.P,
        Key.Q,
        Key.R,
        Key.S,
        Key.T,
        Key.U,
        Key.V,
        Key.W,
        Key.X,
        Key.Y,
        Key.Z,
        Key.Zero,
        Key.One,
        Key.Two,
        Key.Three,
        Key.Four,
        Key.Five,
        Key.Six,
        Key.Seven,
        Key.Eight,
        Key.Nine,
    )
}