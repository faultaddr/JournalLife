package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pyy.journalapp.timemachine.TimeCapsule
import com.pyy.journalapp.timemachine.CapsuleStatus
import kotlinx.datetime.LocalDate

@Composable
fun TimeCapsuleCreator(
    onTimeCapsuleCreated: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

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
                    text = "🔮 时光胶囊",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "将今天的记录封装，在未来的某一天打开",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            selectedDate?.let {
                Text(
                    text = "胶囊将在 $it 打开",
                    style = MaterialTheme.typography.bodyMedium
                )
            } ?: run {
                Text(
                    text = "请选择胶囊开启日期",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showDatePicker = true }
                ) {
                    Text("📅 选择日期")
                }

                Button(
                    onClick = { selectedDate?.let { onTimeCapsuleCreated(it) } },
                    enabled = selectedDate != null
                ) {
                    Text("✨ 创建胶囊")
                }
            }

            // 简化的日期选择器 - 在实际应用中应使用适当的日期选择器
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // 使用当前日期加30天作为示例
                                val futureDate = kotlinx.datetime.Clock.System.now()
                                    .plus(30 * 24 * 60 * 60 * 1000_000_000L) // 30天后
                                    .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                                    .date
                                selectedDate = futureDate
                                showDatePicker = false
                            }
                        ) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDatePicker = false }
                        ) {
                            Text("取消")
                        }
                    }
                ) {
                    // 这化日期选择 - 在实际应用中应使用适当的组件
                    Text(
                        text = "选择未来日期",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsuleList(
    capsules: List<TimeCapsule>,
    onCapsuleClick: (TimeCapsule) -> Unit,
    modifier: Modifier = Modifier
) {
    if (capsules.isEmpty()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "暂无时光胶囊",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(capsules.size) { index ->
            val capsule = capsules[index]
            TimeCapsuleItem(
                capsule = capsule,
                onClick = { onCapsuleClick(capsule) }
            )
        }
    }
}

@Composable
fun TimeCapsuleItem(
    capsule: TimeCapsule,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🔮 时光胶囊",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "目标日期: ${capsule.targetDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (capsule.status) {
                        CapsuleStatus.ACTIVE -> "⏳ 等待开启"
                        CapsuleStatus.DELIVERED -> "✅ 已开启"
                        CapsuleStatus.CANCELLED -> "❌ 已取消"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = when (capsule.status) {
                        CapsuleStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                        CapsuleStatus.DELIVERED -> MaterialTheme.colorScheme.secondary
                        CapsuleStatus.CANCELLED -> MaterialTheme.colorScheme.error
                    }
                )
            }
        }
    }
}