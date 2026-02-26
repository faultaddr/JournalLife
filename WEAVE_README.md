# 织忆 (Weave) - 数字手账应用

## 产品理念

**Slogan**: 指尖的温度，数字的永恒

织忆是一款专注于"创作过程体验"的数字手账应用，它不是效率工具，而是一个数字玩具、心灵避难所。

### 核心转变
- **从"自动填充"到"主动选择"**: AI 提供素材库而非替用户写
- **从"列表视图"到"物理引擎"**: 每个元素都有物理属性（重力、碰撞、卷边、阴影）
- **从"视觉反馈"到"多感官反馈"**: 强调触觉（震动）和听觉（ASMR 音效）

## 技术栈

- **平台**: Android + iOS (Kotlin Multiplatform)
- **UI**: Compose Multiplatform 1.8.2
- **Kotlin**: 2.2.0
- **依赖注入**: Koin
- **本地存储**: SQLDelight 2.0.2
- **时间处理**: kotlinx-datetime
- **设置存储**: multiplatform-settings

## 项目结构

```
composeApp/src/commonMain/kotlin/com/weave/app/
├── core/                          # 核心功能
│   ├── physics/                   # 物理引擎
│   │   └── PhysicsEngine.kt
│   └── haptics/                   # 触觉反馈
│       ├── HapticsManager.kt      # expect 声明
│       └── HapticsManager.android.kt
│       └── HapticsManager.ios.kt
├── data/                          # 数据层
│   ├── model/                     # 数据模型
│   │   └── Models.kt              # Journal, Page, PageElement, Material
│   └── repository/                # Repository 接口
│       ├── JournalRepository.kt
│       ├── PageRepository.kt
│       └── MaterialRepository.kt
├── di/                            # 依赖注入
│   └── Koin.kt
├── ui/                            # UI 层
│   ├── theme/                     # 主题
│   │   ├── Color.kt               # 织忆配色方案
│   │   └── Theme.kt               # MaterialTheme 配置
│   ├── components/                # 可复用组件
│   │   ├── skeuomorphic/          # 拟物化组件
│   │   │   ├── SkeuomorphicComponents.kt
│   │   │   └── PaperSheet.kt
│   │   └── physics/               # 物理组件
│   │       └── PhysicsComponents.kt
│   ├── animation/                 # 动画效果
│   │   ├── PageAnimations.kt      # 翻页动画
│   │   └── EffectAnimations.kt    # 胶带撕裂、显影动画
│   └── screens/                   # 屏幕
│       ├── workbench/             # 工作台首页
│       │   └── WorkbenchScreen.kt
│       ├── editor/                # 页面编辑器
│       │   └── PageEditorScreen.kt
│       ├── gallery/               # 展览模式（书架）
│       │   └── GalleryScreen.kt
│       └── materials/             # 素材库
│           └── MaterialsScreen.kt
└── App.kt                         # 应用入口与导航
```

## 核心功能

### 1. 工作台 (Workbench)
- 木质桌面背景
- 中央当前编辑页面
- 底部实物风格工具栏（笔袋、收纳盒、抽屉）
- 右上角日历夹

### 2. 页面编辑器 (Page Editor)
- 多种纸张样式（空白、横线、网格、点阵、牛皮纸）
- 贴纸拖拽与物理效果
- 胶带撕裂效果
- 拍立得照片显影动画
- 手写文字元素

### 3. 展览模式 (Gallery)
- 3D 书架展示手账本
- 书本厚度和封面样式
- 支持创建新手账本

### 4. 素材库 (Materials)
- 分类浏览（贴纸、胶带、纸张、相框、笔具、印章）
- 稀有度标识
- 每日补给功能

### 5. 物理引擎 (Physics Engine)
- 重力掉落效果
- 拖拽时的旋转和缩放
- 阴影随高度变化
- 着陆时的弹跳效果

### 6. 动画效果
- 页面翻页 3D 动画
- 胶带撕裂不规则边缘
- 拍立得显影动画
- 启动仪式（暗色过渡）

### 7. 触觉反馈
- 轻微震动（Light Impact）
- 中等震动（Medium Impact）
- 强烈震动（Heavy Impact）
- 选择变化反馈

## 数据模型

### Journal（手账本）
```kotlin
data class Journal(
    val id: String,
    val title: String,
    val coverStyle: CoverStyle,
    val createdAt: Long,
    val updatedAt: Long,
    val pageCount: Int,
    val thickness: Float
)
```

### Page（页面）
```kotlin
data class Page(
    val id: String,
    val journalId: String,
    val date: Long,
    val paperStyle: PaperStyle,
    val backgroundColor: Long,
    val elements: List<PageElement>
)
```

### PageElement（页面元素）
密封类，包含：
- `StickerElement` - 贴纸
- `TapeElement` - 胶带
- `PhotoElement` - 照片
- `TextElement` - 文字

### Material（素材）
```kotlin
data class Material(
    val id: String,
    val name: String,
    val category: MaterialCategory,
    val resourcePath: String,
    val rarity: Rarity,
    val isUnlocked: Boolean
)
```

## 主题配色

### 桌面背景
- WoodLight: `#A0826D`
- WoodMedium: `#8B7355`
- WoodDark: `#6B5344`

### 纸张颜色
- PaperWhite: `#FDFCF8`
- PaperCream: `#F5F0E1`
- PaperKraft: `#D4C5A9`

### 强调色
- AccentGold: `#D4AF37`
- AccentCopper: `#B87333`

## 待实现功能

1. **Repository 实现**: 需要实现 JournalRepository、PageRepository、MaterialRepository 的具体 SQLDelight 实现
2. **音效系统**: 需要添加音频管理器（expect/actual）
3. **AI 推荐**: MVP 版本暂不实现，后续可添加本地规则引擎
4. **图片选择**: 集成图片选择器
5. **数据持久化**: 完成 SQLDelight 数据库操作
6. **素材资源**: 添加贴纸、胶带等素材图片资源

## 构建与运行

### Android
```bash
# 需要设置 ANDROID_HOME 环境变量
./gradlew :composeApp:installDebug
```

### iOS
```bash
# 打开 Xcode 项目
open iosApp/iosApp.xcodeproj
```

## 设计原则

1. **尊重"摩擦力"**: 不消除所有步骤，而是让步骤变得有趣
2. **强调"所有权"**: 每个元素都是用户亲手放置的
3. **感官满足**: 视觉（拟物）、听觉（ASMR）、触觉（震动）全方位反馈
4. **情感沉淀**: 通过创作过程建立情感连接

## 开源许可

本项目基于 [Apache License 2.0](LICENSE) 开源。
