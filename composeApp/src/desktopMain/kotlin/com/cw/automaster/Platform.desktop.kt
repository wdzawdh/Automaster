package com.cw.automaster

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.platform.ConfigStore
import com.cw.automaster.platform.FileSelector
import com.cw.automaster.platform.MacConfigStore
import com.cw.automaster.platform.WorkflowManager
import com.cw.automaster.platform.MacFileSelector
import com.cw.automaster.utils.isLinux
import com.cw.automaster.utils.isMac
import com.cw.automaster.utils.isWindows
import com.cw.automaster.platform.MacWorkflowManager
import java.io.File

actual fun getPlatformType(): PlatformType {
    return if (isMac) {
        PlatformType.MAC
    } else if (isWindows) {
        PlatformType.WINDOWS
    } else if (isLinux) {
        PlatformType.LINUX
    } else {
        PlatformType.UNKNOWN
    }
}

actual fun getWorkflowManager(): WorkflowManager? {
    if (isMac) {
        return MacWorkflowManager
    }
    return null
}

actual fun getFileSelector(): FileSelector? {
    if (isMac) {
        return MacFileSelector
    }
    return null
}

actual fun getConfigStore(): ConfigStore? {
    if (isMac) {
        return MacConfigStore
    }
    return null
}