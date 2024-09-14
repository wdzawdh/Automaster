package com.cw.automaster

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.platform.ConfigStore
import com.cw.automaster.platform.FileSelector
import com.cw.automaster.platform.PermissionManager
import com.cw.automaster.platform.KeyValueStore
import com.cw.automaster.platform.ShortcutManager
import com.cw.automaster.platform.WorkflowManager


// platform
expect fun getPlatformType(): PlatformType

// key value
expect fun getKeyValueStore(): KeyValueStore?

// config
expect fun getConfigStore(): ConfigStore?

// selector
expect fun getFileSelector(): FileSelector?

// workflow
expect fun getWorkflowManager(): WorkflowManager?

// permission
expect fun getPermissionManager(): PermissionManager?

// shortcut
expect fun getShortcutManager(): ShortcutManager?