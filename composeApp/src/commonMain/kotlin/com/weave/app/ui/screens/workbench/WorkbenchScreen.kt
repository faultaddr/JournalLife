package com.weave.app.ui.screens.workbench

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.app.data.model.Journal
import com.weave.app.data.model.Page
import com.weave.app.data.model.PageElement
import com.weave.app.data.model.PaperStyle
import com.weave.app.data.repository.JournalRepository
import com.weave.app.data.repository.PageRepository
import com.weave.app.ui.components.skeuomorphic.DrawerButton
import com.weave.app.ui.components.skeuomorphic.NeumorphicButton
import com.weave.app.ui.components.skeuomorphic.PaperSheet
import com.weave.app.ui.components.skeuomorphic.PenCaseButton
import com.weave.app.ui.components.skeuomorphic.ToolBoxButton
import com.weave.app.ui.theme.WeaveColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

/**
 * 工作台 ViewModel
 */
class WorkbenchViewModel(
    private val journalRepository: JournalRepository,
    private val pageRepository: PageRepository
) : ViewModel() {

    private val _currentJournal = MutableStateFlow<Journal?>(null)
    val currentJournal: StateFlow<Journal?> = _currentJournal.asStateFlow()

    private val _currentPage = MutableStateFlow<Page?>(null)
    val currentPage: StateFlow<Page?> = _currentPage.asStateFlow()

    private val _elements = MutableStateFlow<List<PageElement>>(emptyList())
    val elements: StateFlow<List<PageElement>> = _elements.asStateFlow()

    private val _currentDate = MutableStateFlow(Clock.System.now().toEpochMilliseconds())
    val currentDate: StateFlow<Long> = _currentDate.asStateFlow()

    init {
        loadOrCreateTodayPage()
    }

    private fun loadOrCreateTodayPage() {
        viewModelScope.launch {
            // 这里简化处理，实际应该加载默认手账本
            val journals = journalRepository.getAllJournals()
            // 创建或获取今日页面
        }
    }

    fun onDateChange(date: Long) {
        _currentDate.value = date
        // 加载对应日期的页面
    }

    fun onElementAdd(element: PageElement) {
        _elements.value = _elements.value + element
    }

    fun onElementUpdate(element: PageElement) {
        _elements.value = _elements.value.map {
            if (it.id == element.id) element else it
        }
    }

    fun onElementRemove(id: String) {
        _elements.value = _elements.value.filter { it.id != id }
    }
}

/**
 * 工作台首页 - 核心创作空间
 */
@Composable
fun WorkbenchScreen(
    viewModel: WorkbenchViewModel = koinViewModel(),
    onNavigateToGallery: () -> Unit = {},
    onNavigateToMaterials: () -> Unit = {},
    onNavigateToEditor: () -> Unit = {}
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val elements by viewModel.elements.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WeaveColors.WoodLight,
                        WeaveColors.WoodMedium,
                        WeaveColors.WoodDark
                    )
                )
            )
    ) {
        // 中央：当前编辑页面
        CurrentPageSheet(
            page = currentPage,
            elements = elements,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp)
        )

        // 右上角：日历夹
        CalendarClip(
            date = currentDate,
            onClick = { /* 打开日期选择器 */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        // 左上角：标题
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(
                text = "织忆",
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                color = WeaveColors.PaperCream
            )
            Text(
                text = "指尖的温度，数字的永恒",
                fontSize = 12.sp,
                color = WeaveColors.PaperCream.copy(alpha = 0.7f)
            )
        }

        // 底部工具栏
        WorkbenchToolbar(
            onPenCaseClick = onNavigateToMaterials,
            onStorageBoxClick = onNavigateToMaterials,
            onDrawerClick = onNavigateToGallery,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * 当前页面展示
 */
@Composable
private fun CurrentPageSheet(
    page: Page?,
    elements: List<PageElement>,
    modifier: Modifier = Modifier
) {
    PaperSheet(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp),
        backgroundColor = page?.let { WeaveColors.getPaperColor(it.paperStyle) }
            ?: WeaveColors.PaperWhite,
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (page == null || elements.isEmpty()) {
                // 空白页面提示
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "今日页面",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        color = WeaveColors.WoodMedium.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击下方工具开始创作",
                        fontSize = 14.sp,
                        color = WeaveColors.WoodMedium.copy(alpha = 0.3f)
                    )
                }
            } else {
                // 渲染页面元素
                // TODO: 实现 PageEditor
            }
        }
    }
}

/**
 * 日历夹组件
 */
@Composable
private fun CalendarClip(
    date: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val instant = Instant.fromEpochMilliseconds(date)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    NeumorphicButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        elevation = 4.dp,
        backgroundColor = WeaveColors.AccentCopper
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "${localDate.monthNumber}月",
                fontSize = 10.sp,
                color = WeaveColors.PaperWhite.copy(alpha = 0.8f)
            )
            Text(
                text = "${localDate.dayOfMonth}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = WeaveColors.PaperWhite
            )
        }
    }
}

/**
 * 工作台工具栏
 */
@Composable
private fun WorkbenchToolbar(
    onPenCaseClick: () -> Unit,
    onStorageBoxClick: () -> Unit,
    onDrawerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 笔袋
        PenCaseButton(
            onClick = onPenCaseClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "笔袋",
                    tint = WeaveColors.PaperWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 收纳盒（贴纸）
        ToolBoxButton(
            onClick = onStorageBoxClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "收纳盒",
                    tint = WeaveColors.PaperWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // 抽屉（历史归档）
        DrawerButton(
            onClick = onDrawerClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "抽屉",
                    tint = WeaveColors.PaperWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}
