package com.cw.automaster.shortcut

import com.cw.automaster.model.Shortcut


object ShortcutHelper {
    init {
        System.loadLibrary("shortcut_helper")
    }

    private val shortcutMap = mutableMapOf<Int, Shortcut>()
    private var onShortcutPressed: ((shortcut: Shortcut) -> Unit)? = null

    // 注册快捷键，返回快捷键 ID
    external fun registerHotKey(keyCode: Int, modifiers: Int): Int

    // 注销快捷键
    external fun unregisterHotKey(hotKeyID: Int)

    // 处理快捷键按下的回调
    fun onHotKeyPressed(hotKeyID: Int) {
        println("HotKey $hotKeyID pressed!")
        val shortcut = shortcutMap[hotKeyID]
        if (shortcut != null) {
            onShortcutPressed?.invoke(shortcut)
        }
    }

    fun registerShortcut(shortcut: Shortcut): Boolean {
        val keyCode = keyCodeMap[shortcut.key] ?: return false
        val modifiers = shortcut.modifiers?.map { keyCodeMap[it] }?.sumOf { it ?: 0 } ?: -1
        val hotKeyID = registerHotKey(keyCode, modifiers)
        if (hotKeyID != -1) {
            shortcutMap[hotKeyID] = shortcut
            return true
        }
        return false
    }

    fun unregisterShortcut(shortcut: Shortcut) {
        var removeKey: Int? = null
        shortcutMap.forEach { (k, v) ->
            if (v == shortcut) {
                removeKey = k
                return@forEach
            }
        }
        removeKey?.let {
            shortcutMap.remove(it)
            unregisterHotKey(it)
        }
    }

    fun setOnShortcutPressed(onKeyPressed: (shortcut: Shortcut) -> Unit) {
        this.onShortcutPressed = onKeyPressed
    }

    private val keyCodeMap = mapOf(
        // 字母按键
        "A" to 0,
        "S" to 1,
        "D" to 2,
        "F" to 3,
        "H" to 4,
        "G" to 5,
        "Z" to 6,
        "X" to 7,
        "C" to 8,
        "V" to 9,
        "B" to 11,
        "Q" to 12,
        "W" to 13,
        "E" to 14,
        "R" to 15,
        "Y" to 16,
        "T" to 17,
        "1" to 18,
        "2" to 19,
        "3" to 20,
        "4" to 21,
        "6" to 22,
        "5" to 23,
        "=" to 24,
        "9" to 25,
        "7" to 26,
        "-" to 27,
        "8" to 28,
        "0" to 29,
        "]" to 30,
        "O" to 31,
        "U" to 32,
        "[" to 33,
        "I" to 34,
        "P" to 35,
        "L" to 37,
        "J" to 38,
        "'" to 39,
        "K" to 40,
        ";" to 41,
        "\\" to 42,
        "," to 43,
        "/" to 44,
        "N" to 45,
        "M" to 46,
        "." to 47,

        // 修饰键
        "⌘" to 0x100,
        "⇧" to 0x200,
        "⌃" to 0x400,
        "⌥" to 0x800
    )
}