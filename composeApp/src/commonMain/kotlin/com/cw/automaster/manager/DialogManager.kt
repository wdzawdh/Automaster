package com.cw.automaster.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

object DialogManager {
    private var dialogName: String? = null
    private var contentView: (@Composable () -> Unit) = { }
    private var isDialogVisible: MutableState<Boolean>? = null

    fun getContent(isDialogVisible: MutableState<Boolean>): (@Composable () -> Unit)? {
        DialogManager.isDialogVisible = isDialogVisible
        return if (isDialogVisible.value) contentView else null
    }

    fun isShow(name: String? = null): Boolean {
        if (name != null && dialogName != name) {
            return false
        }
        return isDialogVisible?.value ?: false
    }

    fun show(name: String? = null, dialog: (@Composable () -> Unit)? = null) {
        if (dialog != null) {
            dialogName = name
            contentView = dialog
        }
        isDialogVisible?.value = true
    }

    fun dismiss() {
        isDialogVisible?.value = false
    }
}