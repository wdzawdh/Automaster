package com.cw.automaster.platform

import com.cw.automaster.model.Workflow
import com.cw.automaster.tray.TrayManager.addMenuItem
import com.cw.automaster.tray.TrayManager.removeAllItem
import com.cw.automaster.utils.RuntimeUtils
import com.cw.automaster.utils.FileUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.system.exitProcess

object MacWorkflowManager : WorkflowManager {

    private val workflowDirectory =
        File(System.getProperty("user.home"), ".automaster/workflows/local")

    fun initTray() {
        setWorkflowTray(getAllWorkflows())
    }

    /**
     * 获取所有的 Workflow
     */
    override fun getAllWorkflows(): List<Workflow> {
        // 确保文件夹存在
        checkWorkflowDir()
        return workflowDirectory
            .listFiles { file -> file.isDirectory && file.name.endsWith(".workflow") }
            ?.map { Workflow(it.nameWithoutExtension, it.absolutePath, it.lastModified()) }
            ?: emptyList()
    }

    /**
     * 导入 Workflow
     */
    @Throws(IOException::class)
    override fun importWorkflow(workflowPath: String) {
        // 确保文件夹存在
        checkWorkflowDir()
        val sourceWorkflow = File(workflowPath)
        if (sourceWorkflow.exists() && sourceWorkflow.name.endsWith(".workflow")) {
            val targetWorkflow = File(workflowDirectory, sourceWorkflow.name)
            // 拷贝文件夹
            FileUtils.copyDirectory(
                sourceWorkflow.toPath(),
                targetWorkflow.toPath(),
            )
        } else {
            throw IOException("Invalid workflow file.")
        }
        setWorkflowTray(getAllWorkflows())
    }

    /**
     * 导出 Workflow
     */
    @Throws(IOException::class)
    override fun exportWorkflow(workflowPath: String, destinationDirectory: String) {
        // 确保文件夹存在
        checkWorkflowDir()
        val workflow = File(workflowPath)
        if (workflow.exists() && workflow.isDirectory && workflow.name.endsWith(".workflow")) {
            val destinationFile = File(destinationDirectory, workflow.name)
            Files.copy(
                workflow.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING
            )
        } else {
            throw IOException("Workflow not found or invalid.")
        }
    }

    /**
     * 删除指定的 Workflow
     */
    override fun deleteWorkflow(workflowPath: String) {
        // 确保文件夹存在
        checkWorkflowDir()
        val workflow = File(workflowPath)
        if (workflow.exists() && workflow.isDirectory) {
            workflow.deleteRecursively() // 递归删除文件夹
        }
        setWorkflowTray(getAllWorkflows())
    }

    /**
     * 运行 Workflow
     */
    override fun runWorkflow(workflowPath: String): String? {
        val workflow = File(workflowPath)
        if (workflow.exists()) {
            return RuntimeUtils.runCommand("automator $workflowPath")
        }
        return null
    }

    private fun setWorkflowTray(workflows: List<Workflow>) {
        removeAllItem()
        workflows.forEach {
            addMenuItem(it.name) {
                runWorkflow(it.path)
            }
        }
        addMenuItem("退出") {
            exitProcess(0)
        }
    }

    private fun checkWorkflowDir() {
        if (!workflowDirectory.exists()) {
            workflowDirectory.mkdirs()
        }
    }
}