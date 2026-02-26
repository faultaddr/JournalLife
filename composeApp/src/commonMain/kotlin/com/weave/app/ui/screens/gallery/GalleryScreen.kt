package com.weave.app.ui.screens.gallery

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.app.data.model.CoverStyle
import com.weave.app.data.model.Journal
import com.weave.app.data.repository.JournalRepository
import com.weave.app.ui.components.skeuomorphic.NeumorphicButton
import com.weave.app.ui.theme.WeaveColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * 展览模式 ViewModel
 */
class GalleryViewModel(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> = _journals.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadJournals()
    }

    private fun loadJournals() {
        viewModelScope.launch {
            _isLoading.value = true
            journalRepository.getAllJournals().collect {
                _journals.value = it
                _isLoading.value = false
            }
        }
    }

    fun createJournal(title: String, coverStyle: CoverStyle) {
        viewModelScope.launch {
            journalRepository.createJournal(title, coverStyle)
        }
    }

    fun deleteJournal(id: String) {
        viewModelScope.launch {
            journalRepository.deleteJournal(id)
        }
    }
}

/**
 * 展览模式 - 3D 书架
 */
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = koinViewModel(),
    onJournalClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val journals by viewModel.journals.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WeaveColors.WoodDark,
                        WeaveColors.WoodMedium,
                        WeaveColors.WoodDark
                    )
                )
            )
    ) {
        // 标题栏
        GalleryToolbar(
            onBackClick = onBackClick,
            onAddClick = {
                // 显示创建手账本对话框
                viewModel.createJournal(
                    title = "新手账本",
                    coverStyle = CoverStyle.LEATHER_BROWN
                )
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // 书架网格
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {
            items(journals) { journal ->
                JournalBook(
                    journal = journal,
                    onClick = { onJournalClick(journal.id) }
                )
            }

            // 添加新手账本按钮
            item {
                AddJournalButton(
                    onClick = {
                        viewModel.createJournal(
                            title = "新手账本 ${journals.size + 1}",
                            coverStyle = CoverStyle.entries.random()
                        )
                    }
                )
            }
        }
    }
}

/**
 * 书架上的手账本 - 3D 效果
 */
@Composable
private fun JournalBook(
    journal: Journal,
    onClick: () -> Unit
) {
    val rotationY = remember { Animatable(15f) }

    LaunchedEffect(Unit) {
        rotationY.animateTo(
            targetValue = 15f,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        // 3D 书本
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(100.dp)
                .graphicsLayer {
                    this.rotationY = rotationY.value
                    cameraDistance = 12f * density
                }
                .shadow(
                    elevation = journal.thickness.coerceIn(4f, 20f).dp,
                    shape = RoundedCornerShape(2.dp, 4.dp, 4.dp, 2.dp),
                    clip = false
                )
                .background(
                    getCoverColor(journal.coverStyle),
                    RoundedCornerShape(2.dp, 4.dp, 4.dp, 2.dp)
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // 书脊装饰
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    )
            )

            // 标题
            Text(
                text = journal.title.take(4) + if (journal.title.length > 4) "..." else "",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = WeaveColors.PaperWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .graphicsLayer { rotationZ = -90f }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 底部厚度阴影
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(journal.thickness.coerceIn(4f, 16f).dp)
                .background(
                    Color.Black.copy(alpha = 0.3f),
                    RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 手账本名称
        Text(
            text = journal.title,
            fontSize = 12.sp,
            color = WeaveColors.PaperCream,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * 添加新手账本按钮
 */
@Composable
private fun AddJournalButton(
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NeumorphicButton(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            backgroundColor = WeaveColors.WoodLight.copy(alpha = 0.5f)
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加手账本",
                    tint = WeaveColors.PaperWhite,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 占位阴影
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(8.dp)
                .background(
                    Color.Black.copy(alpha = 0.2f),
                    RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "新建手账",
            fontSize = 12.sp,
            color = WeaveColors.PaperCream.copy(alpha = 0.7f)
        )
    }
}

/**
 * 根据封面样式获取颜色
 */
private fun getCoverColor(style: CoverStyle): Color {
    return when (style) {
        CoverStyle.LEATHER_BROWN -> Color(0xFF8B4513)
        CoverStyle.LEATHER_RED -> Color(0xFF8B0000)
        CoverStyle.LEATHER_BLUE -> Color(0xFF00008B)
        CoverStyle.LEATHER_GREEN -> Color(0xFF006400)
        CoverStyle.FABRIC_BEIGE -> Color(0xFFF5F5DC)
        CoverStyle.FABRIC_GRAY -> Color(0xFF808080)
        CoverStyle.CANVAS_KRAFT -> Color(0xFFD4C5A9)
        CoverStyle.VINTAGE_MAP -> Color(0xFFDEB887)
    }
}

/**
 * 展览模式工具栏
 */
@Composable
private fun GalleryToolbar(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "返回",
                tint = WeaveColors.PaperWhite
            )
        }

        Text(
            text = "我的手账",
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = WeaveColors.PaperWhite,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onAddClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "添加",
                tint = WeaveColors.AccentGold
            )
        }
    }
}
