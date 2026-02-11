package com.pyy.journalapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportProgressDialog(
    isVisible: Boolean,
    progress: Int,
    message: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = { /* Prevent dismissing until operation is complete */ },
            confirmButton = {
                Button(
                    onClick = onCancel
                ) {
                    Text("确定")
                }
            },
            title = { Text("导出进度") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(message)
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("$progress%")
                }
            }
        )
    }
}