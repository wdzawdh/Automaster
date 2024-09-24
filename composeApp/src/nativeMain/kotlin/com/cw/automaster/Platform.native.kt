package com.cw.automaster

import com.cw.automaster.platform.KeyValueStore

actual fun getKeyValueStore(): KeyValueStore? {
    return null
}

actual fun openUrl(url: String) {
}