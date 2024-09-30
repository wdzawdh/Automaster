package com.cw.automaster.model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val path: String,
    var name: String? = null,
    var shortcut: Shortcut? = null,
)