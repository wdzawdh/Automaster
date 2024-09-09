package com.cw.automaster.manager

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackbarManager {
    private val snackbarHostState = SnackbarHostState()

    fun getSnackbarHostState(): SnackbarHostState {
        return snackbarHostState
    }

    fun showMessage(
        coroutineScope: CoroutineScope,
        message: String,
        actionLabel: String = "确定",
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message, actionLabel = actionLabel, duration = duration)
        }
    }
}