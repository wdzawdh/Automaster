package com.cw.automaster

import MessageDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import automaster.composeapp.generated.resources.Res
import automaster.composeapp.generated.resources.back
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.Screen
import com.cw.automaster.manager.ScreenManager
import com.cw.automaster.theme.BgColor
import com.cw.automaster.theme.SecondColor
import com.cw.automaster.theme.TextGrey
import com.cw.automaster.widget.TabLayout
import org.jetbrains.compose.resources.painterResource

@Composable
fun SettingsScreen() {
    SettingsPage()
}

@Composable
fun SettingsPage() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().background(color = BgColor)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable { ScreenManager.openScreen(Screen.MAIN) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(15.dp),
                painter = painterResource(Res.drawable.back),
                contentDescription = "back",
                tint = SecondColor
            )
            Text(
                "工具",
                fontSize = 12.sp,
                color = SecondColor,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
        }

        Text(
            "Automaster",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = TextGrey,
            modifier = Modifier.padding(start = 25.dp, bottom = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        ) {
            // Tabs
            val tabs = listOf("设置")
            TabLayout(tabs, selectedTab, Modifier.fillMaxWidth()) { index ->
                selectedTab = index
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            var isGlobalKey by remember {
                mutableStateOf(keyValueStore?.getBoolean(KEY_GLOBAL_SHORTCUT) == true)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 18.dp, end = 12.dp)
            ) {
                Text(
                    "全局快捷键",
                    color = TextGrey,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isGlobalKey,
                    onCheckedChange = { isOpen ->
                        if (isOpen) {
                            if (permissionManager?.checkPermission() == true) {
                                registerKeyboard(true)
                                isGlobalKey = true
                            } else {
                                DialogManager.show {
                                    MessageDialog(
                                        message = "全局快捷键需要添加“辅助功能”权限",
                                        confirmText = "去添加"
                                    ) { confirm ->
                                        DialogManager.dismiss()
                                        if (confirm) {
                                            permissionManager?.requestPermission {
                                                registerKeyboard(true)
                                                isGlobalKey = it
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            registerKeyboard(false)
                            isGlobalKey = false
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SecondColor, // 开启时开关圆点颜色
                    )
                )
            }
        }
    }
}

