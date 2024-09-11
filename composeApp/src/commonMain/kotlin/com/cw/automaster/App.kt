package com.cw.automaster

import SHORTCUT_DIALOG_NAME
import ShortcutDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import automaster.composeapp.generated.resources.Res
import automaster.composeapp.generated.resources.add
import automaster.composeapp.generated.resources.logo
import automaster.composeapp.generated.resources.more
import automaster.composeapp.generated.resources.search
import automaster.composeapp.generated.resources.task
import automaster.composeapp.generated.resources.title
import com.cw.automaster.emum.FileSelectorType
import com.cw.automaster.emum.PlatformType
import com.cw.automaster.manager.ConfigManager
import com.cw.automaster.manager.DialogManager
import com.cw.automaster.manager.LoadingManager
import com.cw.automaster.manager.SnackbarManager
import com.cw.automaster.model.Config
import com.cw.automaster.model.Workflow
import com.cw.automaster.theme.BgColor
import com.cw.automaster.theme.SecondColor
import com.cw.automaster.utils.getCurrentTimestamp
import com.cw.automaster.widget.Loading
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

// global object
val platformType = getPlatformType()
val workflowManager = getWorkflowManager()
val fileSelector = getFileSelector()

@Composable
fun App() {
    // config
    ConfigManager.init()

    Scaffold(
        snackbarHost = {
            // snack bar
            SnackbarHost(SnackbarManager.getSnackbarHostState())
        },
        content = {
            // main page
            MainPage()
            // dialog
            val dialogState = remember { mutableStateOf(false) }
            DialogManager.getContent(dialogState)?.invoke()
            // loading
            val loadingState = remember { mutableStateOf(false) }
            LoadingManager.getContent(loadingState)?.invoke()
            LoadingManager.setContent { Loading() }
        }
    )
}

@Composable
fun MainPage() {
    var selectedTab by remember { mutableStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    var workflows by remember { mutableStateOf(workflowManager?.getAllWorkflows()) }

    Column(
        modifier = Modifier.fillMaxSize().background(color = BgColor)
    ) {
        TopBar {
            workflows = workflowManager?.getAllWorkflows()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tabs
            TabLayout(selectedTab, Modifier.weight(1f)) { index ->
                selectedTab = index
            }

            // Search Box
            SearchBar(searchText = searchText) { newText ->
                searchText = newText
            }
        }

        WorkflowList(workflows ?: listOf(), searchText) {
            workflows = workflowManager?.getAllWorkflows()
        }
    }
}

@Composable
fun TabLayout(selectedTab: Int, modifier: Modifier = Modifier, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("本地") // "本地", "远程", "全部"
    ScrollableTabRow(selectedTabIndex = selectedTab,
        modifier = modifier.then(Modifier.padding(5.dp)),
        contentColor = SecondColor,
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
                    color = Color.Black,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun TopBar(onWorkflowUpdate: () -> Unit) {
    var expanded by remember { mutableStateOf(false) } // 控制下拉框是否展开

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.height(25.dp).wrapContentWidth(),
            tint = SecondColor
        )
        Icon(
            painter = painterResource(Res.drawable.title),
            contentDescription = "title",
            modifier = Modifier.height(30.dp).wrapContentWidth(),
            tint = SecondColor
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { expanded = !expanded }, Modifier.padding(end = 0.dp)) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(Res.drawable.add),
                contentDescription = "add"
            )
        }
        val scope = rememberCoroutineScope()
        // DropdownMenu 实现下拉选项框
        if (expanded) {
            Box(Modifier.wrapContentWidth(Alignment.End)) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = {
                        expanded = false // 隐藏菜单
                    }, // 点击外部时收起下拉菜单
                    modifier = Modifier.width(200.dp), // 设置菜单宽度
                    offset = DpOffset(x = 0.dp, y = 0.dp),
                    properties = PopupProperties()
                ) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        fileSelector?.selectFile(FileSelectorType.WORKFLOW) { path ->
                            if (path.isNotEmpty()) {
                                workflowManager?.importWorkflow(path)
                                onWorkflowUpdate()
                            } else {
                                SnackbarManager.showMessage(scope, "文件格式错误")
                            }
                        }
                    }) {
                        Text("导入本地Workflow", fontSize = 14.sp)
                    }
                    /*DropdownMenuItem(onClick = {
                        expanded = false
                    }) {
                        Text("导入远程Workflow", fontSize = 14.sp)
                    }*/
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    searchText: String, modifier: Modifier = Modifier, onSearchTextChanged: (String) -> Unit
) {
    var showTextField by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(showTextField) {
        if (showTextField) {
            focusRequester.requestFocus()
        } else {
            onSearchTextChanged("")
        }
    }

    val searchModify = Modifier.padding(horizontal = 18.dp).clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null, // 去除波纹效果
    ) {
        showTextField = !showTextField // 切换显示状态
    }
    Row(
        modifier = modifier.then(searchModify),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(15.dp),
            painter = painterResource(Res.drawable.search),
            contentDescription = "search"
        )
        // AnimatedVisibility 用于动画效果的显示与隐藏
        AnimatedVisibility(
            visible = showTextField,
            enter = expandHorizontally(expandFrom = Alignment.Start),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start),
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = {
                    if (it.length <= 15) {
                        onSearchTextChanged(it)
                    }
                },
                singleLine = true,
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                    .padding(8.dp)
                    .focusRequester(focusRequester),
            ) {
                if (searchText.isEmpty()) {
                    Text("搜索Workflow...", color = Color.Gray, fontSize = 12.sp)
                }
                it()
            }
        }
    }
}

@Composable
fun WorkflowList(workflowList: List<Workflow>, searchText: String, onWorkflowUpdate: () -> Unit) {
    val workflows = workflowList.filter {
        it.name.contains(searchText, ignoreCase = true) || it.path.contains(
            searchText, ignoreCase = true
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        items(workflows.size) { index ->
            WorkflowItem(workflows[index], onWorkflowUpdate)
        }
    }
}

@Composable
fun WorkflowItem(workflow: Workflow, onWorkflowUpdate: () -> Unit) {
    var expanded by remember { mutableStateOf(false) } // 控制下拉框是否展开

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 2.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 图标
        Icon(
            painter = painterResource(Res.drawable.task),
            contentDescription = "task",
            modifier = Modifier.size(40.dp).padding(end = 8.dp),
            tint = SecondColor
        )

        // 项目名称和路径
        val config = ConfigManager.getConfigByPath(workflow.path)
        val shortcut = config?.shortcut
        Column(modifier = Modifier.weight(1f)) {
            Text(text = workflow.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            if (shortcut != null) {
                Text(
                    text = shortcut,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 更多选项按钮
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.more),
                contentDescription = "more",
                tint = SecondColor
            )
        }

        val scope = rememberCoroutineScope()
        // DropdownMenu 实现下拉选项框
        if (expanded) {
            Box(Modifier.wrapContentWidth(Alignment.End)) {
                DropdownMenu(
                    expanded = true,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.width(200.dp),
                    offset = DpOffset(x = 0.dp, y = 0.dp),
                    properties = PopupProperties()
                ) {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        GlobalScope.launch {
                            LoadingManager.loading()
                            workflowManager?.runWorkflow(workflow.path)
                            LoadingManager.dismiss()
                        }
                    }) {
                        Text("运行", fontSize = 14.sp)
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        workflowManager?.deleteWorkflow(workflow.path)
                        onWorkflowUpdate()
                    }) {
                        Text("删除", fontSize = 14.sp)
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        fileSelector?.openFolder(workflow.path)
                    }) {
                        Text("打开文件位置", fontSize = 14.sp)
                    }
                    DropdownMenuItem(onClick = {
                        expanded = false
                        fileSelector?.selectFolder { path ->
                            if (path.isNotEmpty()) {
                                try {
                                    workflowManager?.exportWorkflow(workflow.path, path)
                                    SnackbarManager.showMessage(scope, "导出成功")
                                } catch (e: Exception) {
                                    SnackbarManager.showMessage(scope, "导出失败")
                                }
                            } else {
                                SnackbarManager.showMessage(scope, "文件夹选择错误")
                            }
                        }
                    }) {
                        Text("导出", fontSize = 14.sp)
                    }
                    if (platformType == PlatformType.MAC || platformType == PlatformType.WINDOWS || platformType == PlatformType.LINUX) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            DialogManager.show(SHORTCUT_DIALOG_NAME) {
                                val updateConfig = config ?: Config(workflow.path)
                                ShortcutDialog(updateConfig.shortcut) { shortcut ->
                                    DialogManager.dismiss()
                                    if (shortcut != null) {
                                        updateConfig.shortcut = shortcut
                                        ConfigManager.addOrUpdateConfig(updateConfig)
                                        workflow.lastModified = getCurrentTimestamp()
                                        onWorkflowUpdate()
                                    }
                                }
                            }
                        }) {
                            Text("设置快捷键", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}