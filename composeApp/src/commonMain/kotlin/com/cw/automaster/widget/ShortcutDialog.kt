import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.cw.automaster.theme.ThemeColor
import com.cw.automaster.utils.ShortcutUtils

const val SHORTCUT_DIALOG_NAME = "ShortcutDialog"

@Composable
fun ShortcutDialog(
    shortcutKey: String? = null,
    onShortcutSet: (String?) -> Unit
) {
    var keyString by remember { mutableStateOf(shortcutKey) }
    AlertDialog(
        onDismissRequest = { onShortcutSet(null) },
        title = { Text("") },
        text = {
            val focusRequester = remember { FocusRequester() }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .focusable()
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown) {
                            val shortcut = ShortcutUtils.getShortcut(keyEvent)
                            if (shortcut != null) {
                                keyString = shortcut
                                return@onKeyEvent true
                            }
                        }
                        false
                    }
            ) {
                Text(
                    text = keyString ?: "按下快捷键",
                    fontSize = 20.sp,
                )
            }

            // Request focus when the dialog is first shown
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onShortcutSet(keyString) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ThemeColor
                )
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onShortcutSet(null) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ThemeColor
                )
            ) {
                Text("取消")
            }
        }
    )
}