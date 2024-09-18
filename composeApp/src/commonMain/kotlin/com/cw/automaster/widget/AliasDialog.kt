package com.cw.automaster.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cw.automaster.theme.ThemeColor

const val ALIAS_DIALOG_NAME = "AliasDialog"

@Composable
fun TextFieldDialog(
    title: String? = null,
    normal: String? = null,
    onClickButton: (text: String?) -> Unit
) {
    val alias = remember { mutableStateOf(normal) }
    val maxAliasLength = 10

    AlertDialog(
        onDismissRequest = { onClickButton(null) },
        title = { if (title != null) Text(title) },
        text = {
            Column {
                EditText(
                    value = alias.value ?: "",
                    onValueChange = {
                        if (it.length <= maxAliasLength) alias.value = it
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = ThemeColor
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${alias.value?.length ?: 0}/$maxAliasLength",
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onClickButton(alias.value) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ThemeColor
                )
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onClickButton(null) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ThemeColor
                )
            ) {
                Text("取消")
            }
        }
    )
}