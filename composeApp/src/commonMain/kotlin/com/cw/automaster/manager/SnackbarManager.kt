package com.cw.automaster.manager

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
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
        duration: SnackbarDuration = SnackbarDuration.Short,
        onDismiss: () -> Unit = {},
        onActionClick: () -> Unit = {},
    ) {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message,
                actionLabel = actionLabel,
                duration = duration
            )
            // 监听 actionLabel 点击事件
            when (result) {
                SnackbarResult.ActionPerformed -> onActionClick()
                SnackbarResult.Dismissed -> onDismiss()
            }
        }
    }
}