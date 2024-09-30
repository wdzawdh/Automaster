package com.cw.automaster

import com.cw.automaster.platform.KeyValueStore
import com.cw.automaster.platform.ShortcutManager

actual fun getKeyValueStore(): KeyValueStore? {
    return null
}

actual fun openUrl(url: String) {
}

actual fun getShortcutManager(): ShortcutManager? {
    return null
}