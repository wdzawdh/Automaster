import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cw.automaster.theme.ThemeColor
import com.cw.automaster.theme.TextBlack


@Composable
fun MessageDialog(
    title: String? = "提示",
    message: String? = null,
    confirmText: String = "确认",
    cancelText: String = "取消",
    onClickButton: (confirm: Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onClickButton(false) },
        title = {
            if (title != null) Text(
                title,
                color = TextBlack,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            if (message != null)
                Text(
                    message,
                    color = TextBlack,
                    fontSize = 14.sp
                )
        },
        confirmButton = {
            TextButton(
                onClick = { onClickButton(true) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ThemeColor
                )
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onClickButton(false) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ThemeColor
                )
            ) {
                Text(cancelText)
            }
        }
    )
}