package com.cw.automaster

import com.cw.automaster.platform.PermissionManager
import com.cw.automaster.platform.KeyValueStore
import com.cw.automaster.platform.ShortcutManager

actual fun getPermissionManager(): PermissionManager? {
    return null
}

actual fun getShortcutManager(): ShortcutManager? {
    return null
}

actual fun getKeyValueStore(): KeyValueStore? {
    return null
}