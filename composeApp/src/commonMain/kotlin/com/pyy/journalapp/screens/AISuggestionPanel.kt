package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.ai.ContentAnalysis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISuggestionPanel(
    contentAnalysis: ContentAnalysis?,
    onSuggestionClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (contentAnalysis == null) return

    Card(
        modifier = modifier.fillMaxWidth(),
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
                Text(
                    text = "✨", // 使用表情符号代替 AutoAwesome 图标
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "AI智能建议",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 关键词标签
            if (contentAnalysis.keywords.isNotEmpty()) {
                Text(
                    text = "相关关键词:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contentAnalysis.keywords) { keyword ->
                        SuggestionChip(
                            onClick = { onSuggestionClick(keyword) },
                            label = { Text(keyword) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 情绪分析
            if (contentAnalysis.emotions.isNotEmpty()) {
                Text(
                    text = "情绪分析:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contentAnalysis.emotions) { emotion ->
                        val emotionText = when (emotion) {
                            "positive" -> "😊 积极"
                            "negative" -> "😔 消极"
                            "calm" -> "😐 平静"
                            "neutral" -> "😐 中性"
                            else -> emotion
                        }
                        SuggestionChip(
                            onClick = { onSuggestionClick("Emotion: $emotion") },
                            label = { Text(emotionText) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 话题分类
            if (contentAnalysis.topics.isNotEmpty()) {
                Text(
                    text = "相关话题:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contentAnalysis.topics) { topic ->
                        SuggestionChip(
                            onClick = { onSuggestionClick(topic) },
                            label = { Text(topic) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // AI建议
            if (contentAnalysis.suggestions.isNotEmpty()) {
                Text(
                    text = "AI建议:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(contentAnalysis.suggestions) { suggestion ->
                        InputChip(
                            onClick = { onSuggestionClick(suggestion) },
                            label = { Text(suggestion) }
                        )
                    }
                }
            }
        }
    }
}