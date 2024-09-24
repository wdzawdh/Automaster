package com.cw.automaster.model

import androidx.compose.runtime.MutableState

data class Setting(
    val title: String,
    val isOpen: MutableState<Boolean>,
    val onToggle: (isOpen: Boolean) -> Unit,
)
