package com.cw.automaster.widget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cw.automaster.theme.ThemeColor
import com.cw.automaster.theme.TextBlack

@Composable
fun TabLayout(
    tabs: List<String>,
    selectedTab: Int,
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(selectedTabIndex = selectedTab,
        modifier = modifier.then(Modifier.padding(5.dp)),
        contentColor = ThemeColor,
        backgroundColor = Color.Transparent,
        edgePadding = 0.dp,
        divider = { },
        indicator = { tabPositions ->
            val currentTabPosition = tabPositions[selectedTab]
            //修改指示器长度
            val currentTabWidth by animateDpAsState(
                targetValue = currentTabPosition.width / 4,
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
            )
            //修改指示器偏移量为居中
            val indicatorOffset by animateDpAsState(
                targetValue = currentTabPosition.left + (currentTabPosition.width / 2 - currentTabWidth / 2),
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
            )
            //自带的Indicator指示器，只需改Modifier就可以了
            TabRowDefaults.Indicator(
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.BottomStart)
                    .offset(x = indicatorOffset).width(currentTabWidth).height(2.dp)
            )
        }) {
        tabs.forEachIndexed { index, title ->
            Tab(selected = selectedTab == index, onClick = { onTabSelected(index) }) {
                Text(
                    title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}