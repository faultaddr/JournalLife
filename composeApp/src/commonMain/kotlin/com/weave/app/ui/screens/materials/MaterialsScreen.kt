package com.weave.app.ui.screens.materials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.compose.rememberAsyncImagePainter
import com.weave.app.data.model.Material
import com.weave.app.data.model.MaterialCategory
import com.weave.app.data.model.Rarity
import com.weave.app.data.repository.MaterialRepository
import com.weave.app.ui.components.skeuomorphic.StickerCard
import com.weave.app.ui.theme.WeaveColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * 素材库 ViewModel
 */
class MaterialsViewModel(
    private val materialRepository: MaterialRepository
) : ViewModel() {

    private val _materials = MutableStateFlow<List<Material>>(emptyList())
    val materials: StateFlow<List<Material>> = _materials.asStateFlow()

    private val _selectedCategory = MutableStateFlow<MaterialCategory?>(null)
    val selectedCategory: StateFlow<MaterialCategory?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMaterials()
    }

    private fun loadMaterials() {
        viewModelScope.launch {
            _isLoading.value = true
            materialRepository.getAllMaterials().collect {
                _materials.value = it
                _isLoading.value = false
            }
        }
    }

    fun onCategorySelect(category: MaterialCategory?) {
        _selectedCategory.value = category
        viewModelScope.launch {
            val flow = if (category == null) {
                materialRepository.getAllMaterials()
            } else {
                materialRepository.getMaterialsByCategory(category)
            }
            flow.collect {
                _materials.value = it
            }
        }
    }

    fun onMaterialSelect(material: Material) {
        // 处理素材选择，添加到当前页面
    }

    fun checkDailyRewards() {
        viewModelScope.launch {
            val newMaterials = materialRepository.checkDailyRewards()
            if (newMaterials.isNotEmpty()) {
                // 显示获得新素材提示
            }
        }
    }
}

/**
 * 素材库屏幕
 */
@Composable
fun MaterialsScreen(
    viewModel: MaterialsViewModel = koinViewModel(),
    onMaterialSelect: (Material) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val materials by viewModel.materials.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        WeaveColors.FabricBeige,
                        WeaveColors.PaperCream,
                        WeaveColors.FabricBeige
                    )
                )
            )
    ) {
        // 工具栏
        MaterialsToolbar(
            onBackClick = onBackClick,
            onDailyRewardClick = { viewModel.checkDailyRewards() },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {
            // 分类标签
            CategoryTabs(
                selected = selectedCategory,
                onSelect = viewModel::onCategorySelect,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 素材网格
            LazyVerticalGrid(
                columns = GridCells.Adaptive(80.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(materials) { material ->
                    MaterialItem(
                        material = material,
                        onClick = {
                            viewModel.onMaterialSelect(material)
                            onMaterialSelect(material)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 分类标签
 */
@Composable
private fun CategoryTabs(
    selected: MaterialCategory?,
    onSelect: (MaterialCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 全部
        CategoryTab(
            label = "全部",
            isSelected = selected == null,
            onClick = { onSelect(null) }
        )

        // 各分类
        MaterialCategory.entries.forEach { category ->
            CategoryTab(
                label = getCategoryLabel(category),
                isSelected = selected == category,
                onClick = { onSelect(category) }
            )
        }
    }
}

/**
 * 单个分类标签
 */
@Composable
private fun CategoryTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = if (isSelected) 4.dp else 2.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) WeaveColors.AccentCopper else WeaveColors.PaperWhite,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) WeaveColors.PaperWhite else WeaveColors.InkBlack
        )
    }
}

/**
 * 素材项
 */
@Composable
private fun MaterialItem(
    material: Material,
    onClick: () -> Unit
) {
    StickerCard(
        onClick = onClick,
        modifier = Modifier.size(80.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 素材图片
            Image(
                painter = rememberAsyncImagePainter(material.resourcePath),
                contentDescription = material.name,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Fit,
                alpha = if (material.isUnlocked) 1f else 0.3f
            )

            // 稀有度标识
            if (material.isUnlocked) {
                RarityIndicator(
                    rarity = material.rarity,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            } else {
                // 未解锁遮罩
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "未解锁",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * 稀有度指示器
 */
@Composable
private fun RarityIndicator(
    rarity: Rarity,
    modifier: Modifier = Modifier
) {
    val color = when (rarity) {
        Rarity.COMMON -> Color.Gray
        Rarity.RARE -> Color(0xFF4169E1)
        Rarity.EPIC -> Color(0xFF9400D3)
        Rarity.LEGENDARY -> Color(0xFFFFD700)
    }

    Box(
        modifier = modifier
            .size(12.dp)
            .background(color, CircleShape)
            .shadow(2.dp, CircleShape)
    )
}

/**
 * 获取分类标签
 */
private fun getCategoryLabel(category: MaterialCategory): String {
    return when (category) {
        MaterialCategory.STICKER -> "贴纸"
        MaterialCategory.TAPE -> "胶带"
        MaterialCategory.PAPER -> "纸张"
        MaterialCategory.FRAME -> "相框"
        MaterialCategory.PEN -> "笔具"
        MaterialCategory.STAMP -> "印章"
    }
}

/**
 * 素材库工具栏
 */
@Composable
private fun MaterialsToolbar(
    onBackClick: () -> Unit,
    onDailyRewardClick: () -> Unit,
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
                tint = WeaveColors.InkBlack
            )
        }

        Text(
            text = "素材库",
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = WeaveColors.InkBlack,
            modifier = Modifier.align(Alignment.Center)
        )

        // 每日补给按钮
        IconButton(
            onClick = onDailyRewardClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "每日补给",
                tint = WeaveColors.AccentGold
            )
        }
    }
}
