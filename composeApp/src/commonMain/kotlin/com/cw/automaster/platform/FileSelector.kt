package com.cw.automaster.platform

import com.cw.automaster.emum.FileSelectorType

interface FileSelector {
    fun selectFile(
        fileSelectorType: FileSelectorType, onFileSelected: (String) -> Unit
    )

    fun selectFolder(
        onFolderSelected: (String) -> Unit
    )

    fun openFolder(path: String)
}