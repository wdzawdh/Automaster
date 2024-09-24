package com.cw.automaster

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.platform.ConfigStore
import com.cw.automaster.platform.FileSelector
import com.cw.automaster.platform.KeyValueStore
import com.cw.automaster.platform.WorkflowManager

actual fun getPlatformType(): PlatformType {
    return PlatformType.WEB
}

actual fun getWorkflowManager(): WorkflowManager? {
    return null
}

actual fun getFileSelector(): FileSelector? {
    return null
}

actual fun getConfigStore(): ConfigStore? {
    return null
}

actual fun getKeyValueStore(): KeyValueStore? {
    return null
}

actual fun openUrl(url: String) {
}