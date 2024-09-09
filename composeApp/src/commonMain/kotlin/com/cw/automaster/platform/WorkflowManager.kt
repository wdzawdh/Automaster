package com.cw.automaster.platform

import com.cw.automaster.model.Workflow

interface WorkflowManager {
    fun getAllWorkflows(): List<Workflow>
    fun importWorkflow(workflowPath: String)
    fun exportWorkflow(workflowPath: String, destinationDirectory: String)
    fun deleteWorkflow(workflowPath: String)
    fun runWorkflow(workflowPath: String): String?
}