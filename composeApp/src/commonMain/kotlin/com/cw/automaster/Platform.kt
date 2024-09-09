package com.cw.automaster

import com.cw.automaster.emum.PlatformType
import com.cw.automaster.platform.ConfigStore
import com.cw.automaster.platform.FileSelector
import com.cw.automaster.platform.WorkflowManager


// platform
expect fun getPlatformType(): PlatformType

// workflow
expect fun getWorkflowManager(): WorkflowManager?

// selector
expect fun getFileSelector(): FileSelector?

// config
expect fun getConfigStore(): ConfigStore?