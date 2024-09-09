package com.cw.automaster.platform

import androidx.compose.ui.awt.ComposeWindow
import com.cw.automaster.emum.FileSelectorType
import java.awt.Desktop
import java.awt.FileDialog
import java.io.File
import java.io.IOException

object MacFileSelector : FileSelector {
    /**
     * 显示文件选择器
     * @param fileSelectorType 文件选择类型
     * @param onFileSelected 选择回调
     */
    override fun selectFile(
        fileSelectorType: FileSelectorType, onFileSelected: (String) -> Unit
    ) {
        System.setProperty("apple.awt.use-file-dialog-packages", "true")
        val fileDialog = FileDialog(ComposeWindow())
        fileDialog.isMultipleMode = false
        fileDialog.isVisible = true
        fileDialog.setFilenameFilter { _, name ->
            if (fileSelectorType == FileSelectorType.WORKFLOW) {
                return@setFilenameFilter name.endsWith(".workflow")
            }
            return@setFilenameFilter false
        }
        val directory = fileDialog.directory
        val file = fileDialog.file
        if (directory != null && file != null) {
            if (fileSelectorType == FileSelectorType.WORKFLOW && file.endsWith(".workflow")) {
                onFileSelected("$directory$file")
            } else {
                onFileSelected("")
            }
        }
        System.setProperty("apple.awt.use-file-dialog-packages", "false")
    }

    /**
     * 显示文件夹选择器
     * @param onFolderSelected 选择回调
     */
    override fun selectFolder(
        onFolderSelected: (String) -> Unit
    ) {
        System.setProperty("apple.awt.fileDialogForDirectories", "true")
        val fileDialog = FileDialog(ComposeWindow())
        fileDialog.isMultipleMode = false
        fileDialog.isVisible = true
        val directory = fileDialog.directory
        val file = fileDialog.file
        if (directory != null && file != null) {
            onFolderSelected("$directory$file")
        }
        System.setProperty("apple.awt.fileDialogForDirectories", "false")
    }

    override fun openFolder(path: String) {
        try {
            val folder = File(path)
            if (!folder.exists()) {
                return
            }
            if (Desktop.isDesktopSupported()) {
                val desktop = Desktop.getDesktop()
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    if (folder.isFile) {
                        desktop.open(folder)
                    } else {
                        desktop.open(folder.parentFile)
                    }
                } else {
                    println("Open action is not supported on this platform.")
                }
            } else {
                println("Desktop is not supported on this platform.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}