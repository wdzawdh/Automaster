package com.cw.automaster.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import automaster.composeapp.generated.resources.bug
import automaster.composeapp.generated.resources.logo
import com.cw.automaster.BuildConfig
import com.cw.automaster.URL_NEW_ISSUES
import com.cw.automaster.manager.Screen
import com.cw.automaster.manager.ScreenManager
import com.cw.automaster.openUrl
import com.cw.automaster.settingItems
import com.cw.automaster.theme.BgColor
import com.cw.automaster.theme.TextBlack
import com.cw.automaster.theme.TextBlue
import com.cw.automaster.theme.TextGrey
import com.cw.automaster.theme.ThemeColor
import com.cw.automaster.widget.TabLayout
import org.jetbrains.compose.resources.painterResource

@Composable
fun ToolScreen() {
    ToolPage()
}

@Composable
fun ToolPage() {
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
                tint = ThemeColor
            )
            Text(
                "工具",
                fontSize = 12.sp,
                color = ThemeColor,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
        }

        Text(
            "Automaster",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = TextBlack,
            modifier = Modifier.padding(start = 25.dp, bottom = 12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        ) {
            // Tabs
            val tabs = listOf("设置", "关于")
            TabLayout(tabs, selectedTab, Modifier.fillMaxWidth()) { index ->
                selectedTab = index
            }
        }

        when (selectedTab) {
            0 -> SettingScreen()
            1 -> AboutScreen()
        }
    }
}

@Composable
private fun SettingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        settingItems.forEach {
            SettingItem(it.title, it.isOpen, it.onToggle)
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    checked: MutableState<Boolean>,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 18.dp, end = 12.dp)
    ) {
        Text(
            title,
            color = TextBlack,
            fontSize = 14.sp,
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = checked.value,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ThemeColor, // 开启时开关圆点颜色
            )
        )
    }
}

@Composable
private fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.height(80.dp).wrapContentWidth().padding(start = 15.dp),
            )
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    "Automaster",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    BuildConfig.VERSION_NAME,
                    fontSize = 15.sp,
                    color = TextGrey,
                )
            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
        )
        Row(
            modifier = Modifier.padding(20.dp).clickable { openUrl(URL_NEW_ISSUES) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.bug),
                contentDescription = "bug",
                modifier = Modifier.height(15.dp).wrapContentWidth(),
                tint = TextBlue
            )
            Text(
                "报告问题 ↗",
                fontSize = 13.sp,
                color = TextBlue,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

