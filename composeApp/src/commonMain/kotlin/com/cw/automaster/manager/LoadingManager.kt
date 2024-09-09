package com.cw.automaster.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

object LoadingManager {
    private var contentView: (@Composable () -> Unit) = { }
    private var isLoading: MutableState<Boolean>? = null

    fun getContent(isDialogVisible: MutableState<Boolean>): (@Composable () -> Unit)? {
        isLoading = isDialogVisible
        return if (isDialogVisible.value) contentView else null
    }

    fun setContent(contentView: (@Composable () -> Unit) = { }) {
        this.contentView = contentView
    }

    fun loading() {
        isLoading?.value = true
    }

    fun dismiss() {
        isLoading?.value = false
    }
}