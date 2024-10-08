package com.cw.automaster

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.platform.ConfigStore
import com.cw.automaster.platform.FileSelector
import com.cw.automaster.platform.KeyValueStore
import com.cw.automaster.platform.ShortcutManager
import com.cw.automaster.platform.WorkflowManager


// platform
expect fun getPlatformType(): PlatformType

// openUrl
expect fun openUrl(url: String)

// key value
expect fun getKeyValueStore(): KeyValueStore?

// config
expect fun getConfigStore(): ConfigStore?

// selector
expect fun getFileSelector(): FileSelector?

// workflow
expect fun getWorkflowManager(): WorkflowManager?

// shortcut
expect fun getShortcutManager(): ShortcutManager?