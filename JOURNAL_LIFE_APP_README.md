# JournalLifeApp - 智能日记应用

JournalLifeApp 是一款跨平台的智能日记应用，结合了AI智能联想、时光胶囊和情境化创作三大核心功能，旨在为用户提供更智能、更个性化的日记体验。

## 🌟 核心功能

### 1. AI智能联想
- **内容分析**：自动分析日记内容，识别关键词、情绪和主题
- **智能标签**：自动生成相关标签，便于分类和检索
- **写作建议**：基于内容提供个性化写作建议和改进意见
- **内容洞察**：深入分析用户的写作习惯和偏好

### 2. 时光胶囊
- **未来通信**：将当前的日记条目封存，设定在未来特定日期打开
- **纪念提醒**：自动提醒用户过去的同月同日发生的事件
- **时间穿越**：让用户与过去的自己进行对话，回顾成长历程
- **预期管理**：记录对未来的期望，在到达预定时间时进行回顾

### 3. 情境化创作
- **智能模板推荐**：根据当前情境（如情绪、位置、季节）推荐最适合的写作模板
- **个性化体验**：基于用户当前状态定制专属的日记结构
- **多场景支持**：支持旅行、生日、工作、健康等多种生活场景
- **动态适应**：随着用户使用习惯的变化而调整推荐策略

## 📱 技术特性

- **跨平台支持**：基于Kotlin Multiplatform，支持Android、iOS、桌面端
- **响应式UI**：使用Jetpack Compose和Compose Multiplatform构建现代化界面
- **本地优先**：优先考虑本地存储，确保用户数据隐私
- **离线可用**：即使在没有网络的情况下也能正常使用

## 🏗️ 架构概览

```
JournalLifeApp
├── shared/                 # 共享业务逻辑
│   ├── commonMain/
│   │   └── com.pyylifeapp/
│   │       ├── ai/          # AI智能联想到引擎
│   │       ├── timemachine/ # 时光胶囊功能
│   │       ├── templates/   # 情境化模板系统
│   │       ├── core/        # 核心功能整合
│   │       └── models/      # 数据模型
├── composeApp/             # 跨平台UI
│   └── commonMain/
│       └── com.pyylifeapp/
│           └── App.kt      # 主应用入口
└── server/                 # 后端服务（可选）
    └── src/main/kotlin/
```

## 🚀 快速开始

### 环境要求
- JDK 11 或更高版本
- Kotlin 1.9+
- Gradle 8.0+

### 构建和运行

#### Android
```bash
./gradlew :composeApp:assembleDebug
```

#### 桌面 (JVM)
```bash
./gradlew :composeApp:run
```

#### iOS
打开 `iosApp/` 目录并在Xcode中运行

#### 服务器
```bash
./gradlew :server:run
```

## 📋 功能详情

### AI智能联想功能详解
1. **关键词提取**：基于文本分析技术，自动提取文章中的关键词
2. **情绪识别**：识别文本中的情感倾向（正面、负面、中性等）
3. **主题分类**：将日记归类到不同的主题（工作、生活、学习等）
4. **智能建议**：根据内容提供改进建议和写作灵感

### 时光胶囊功能详解
1. **创建胶囊**：选择日记条目并设定未来的开启时间
2. **到期提醒**：系统在预设日期提醒用户查看时光胶囊
3. **历史回望**：显示同月同日的历史日记，回顾往年今昔
4. **时间规划**：帮助用户规划未来目标并届时回顾

### 情境化创作功能详解
1. **情境感知**：根据用户的位置、时间、设备传感器等信息判断当前情境
2. **模板匹配**：从预设模板库中匹配最符合当前情境的写作模板
3. **个性化定制**：根据用户的偏好和历史行为调整模板推荐
4. **实时调整**：在写作过程中动态调整模板和建议

## 🔧 集成示例

以下是如何在应用中集成三大核心功能的示例：

```kotlin
// 初始化JournalLifeApp核心功能
val journalLifeCore = JournalLifeCore()

// 创建日记条目
val journalEntry = JournalEntry(
    id = "entry-id",
    title = "今天的体验",
    blocks = listOf(/* ... */)
)

// AI智能联想分析
val analysis = journalLifeCore.analyzeJournalContent(journalEntry)
println("关键词: ${analysis.keywords}")

// 情境化模板推荐
val context = WritingContext(
    mood = Mood.HAPPY,
    isTraveling = true
)
val template = journalLifeCore.recommendTemplate(context)

// 创建时光胶囊
val targetDate = Clock.System.now().plus(365.days).toLocalDateTime(TimeZone.currentSystemDefault()).date
val capsule = journalLifeCore.createTimeCapsule(journalEntry, targetDate)
```

## 🎯 应用场景

1. **个人成长记录**：追踪个人成长轨迹，定期回顾
2. **目标规划管理**：设定目标并在未来某个时间点检查进展
3. **生活片段珍藏**：保存珍贵的生活片段和回忆
4. **情感管理**：通过AI分析帮助理解和管理情绪
5. **旅行记录**：利用情境化模板记录旅行见闻

## 🤝 贡献

我们欢迎各种形式的贡献，包括但不限于：
- 功能建议和需求提交
- Bug报告和修复
- 文档完善
- 新特性的开发

## 📄 许可证

本项目遵循 MIT 许可证。