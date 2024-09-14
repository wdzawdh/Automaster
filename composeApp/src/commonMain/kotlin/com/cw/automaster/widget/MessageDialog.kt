import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cw.automaster.theme.SecondColor
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
        title = { if (title != null) Text(title, color = TextBlack, fontSize = 16.sp) },
        text = { if (message != null) Text(message, color = TextBlack, fontSize = 14.sp) },
        confirmButton = {
            Button(
                onClick = { onClickButton(true) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = SecondColor,
                    contentColor = Color.White
                ),
                modifier = Modifier.height(40.dp).padding(bottom = 10.dp)
            ) {
                Text(confirmText, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            }
        },
        dismissButton = {
            Button(
                onClick = { onClickButton(false) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF5F5F5),
                    contentColor = Color.Black
                ),
                modifier = Modifier.height(40.dp).padding(bottom = 10.dp)
            ) {
                Text(cancelText, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            }
        }
    )
}