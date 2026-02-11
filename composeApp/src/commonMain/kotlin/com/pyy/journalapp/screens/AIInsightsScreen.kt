package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.ai.ContentAnalysis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIInsightsScreen(
    onBackClick: () -> Unit,
    contentAnalyses: List<ContentAnalysis> = emptyList(), // 实际应用中这里会有真实的分析数据
    modifier: Modifier = Modifier
) {
    // 示例数据 - 实际应用中这些数据来自于用户的历史日记分析
    val sampleKeywords = listOf("旅行", "工作", "健康", "学习", "情感", "成长", "快乐", "探索", "挑战", "收获")
    val sampleEmotions = listOf("积极向上", "平静", "偶有焦虑", "充满希望")
    val sampleTopics = listOf("职场发展", "健康生活", "亲子关系", "个人成长", "财务管理")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 洞察") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("<-") // 返回按钮
                    }
                },
                actions = {
                    IconButton(onClick = { /* Refresh insights */ }) {
                        Text("🔄") // 刷新按钮
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 情绪趋势
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShowChart,
                                contentDescription = "Mood Trend",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "情绪趋势",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "最近你的情绪总体较为积极，偶有波动，建议关注心理健康。",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // 关键词分析
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = "Keywords",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "关键词分析",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sampleKeywords) { keyword ->
                                AssistChip(
                                    onClick = { /* Handle keyword click */ },
                                    label = { Text(keyword) }
                                )
                            }
                        }
                    }
                }
            }

            // 话题分布
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = "Topics",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "话题分布",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sampleTopics) { topic ->
                                InputChip(
                                    onClick = { /* Handle topic click */ },
                                    label = { Text(topic) }
                                )
                            }
                        }
                    }
                }
            }

            // AI 建议
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Suggestions",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "AI 建议",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                ListItem(
                                    headlineContent = { Text("继续坚持运动") },
                                    supportingContent = { Text("数据显示你最近运动频率较高，有助于保持良好心态") }
                                )
                            }
                            item {
                                ListItem(
                                    headlineContent = { Text("关注工作与生活平衡") },
                                    supportingContent = { Text("最近工作相关记录较多，建议合理安排休息时间") }
                                )
                            }
                            item {
                                ListItem(
                                    headlineContent = { Text("增加社交活动") },
                                    supportingContent = { Text("最近独处时间较长，可以考虑增加社交活动") }
                                )
                            }
                        }
                    }
                }
            }

            // 写作模式分析
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = "Writing Patterns",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "写作模式",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "你最喜欢在晚上 8-10 点之间写作，平均每周记录 3-4 次。\n最长的一次连续写作时间为 2 小时 15 分钟。",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}