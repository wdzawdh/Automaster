package com.cw.automaster.model

data class Workflow(
    val name: String,
    val path: String,
    var lastModified: Long
)