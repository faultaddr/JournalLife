package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.auth.AuthService
import com.pyy.journalapp.repository.JournalRepository
import com.pyy.journalapp.models.JournalEntry
import com.pyy.journalapp.utils.ExportManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExportViewModel(
    private val journalRepository: JournalRepository,
    private val authService: AuthService,
    private val exportManager: ExportManager = ExportManager()
) : ViewModel() {

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting

    private val _exportResult = MutableStateFlow<String?>(null)
    val exportResult: StateFlow<String?> = _exportResult

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress

    fun exportJournalAsImage(journalId: String) {
        viewModelScope.launch {
            try {
                _isExporting.value = true
                _error.value = null
                _exportResult.value = null
                _progress.value = 0

                // In a real app, this would convert the journal to an image
                // For now, we'll simulate the process
                delayExportSimulation()

                _exportResult.value = "Journal exported as image successfully"
                _isExporting.value = false
                _progress.value = 100
            } catch (e: Exception) {
                _error.value = e.message
                _isExporting.value = false
            }
        }
    }

    fun exportJournalAsText(journalId: String) {
        viewModelScope.launch {
            try {
                _isExporting.value = true
                _error.value = null
                _exportResult.value = null
                _progress.value = 0

                val journal = journalRepository.getJournalEntryById(journalId)
                if (journal != null) {
                    // Convert journal to text/markdown format
                    val markdownContent = convertJournalToMarkdown(journal)

                    // In a real app, this would save the content to a file
                    // For now, we'll just store it in the result
                    _exportResult.value = "Journal exported as text successfully:\n\n$markdownContent"
                }

                _isExporting.value = false
                _progress.value = 100
            } catch (e: Exception) {
                _error.value = e.message
                _isExporting.value = false
            }
        }
    }

    /**
     * 导出书籍中所有图片
     */
    fun exportImagesFromBook(bookId: String) {
        viewModelScope.launch {
            try {
                _isExporting.value = true
                _error.value = null
                _exportResult.value = null
                _progress.value = 0

                // 获取书籍下的所有日记条目
                val journalEntries = journalRepository.getJournalEntriesByBookId(bookId)

                // 提取所有图片
                val images = exportManager.extractImagesFromJournalEntries(journalEntries)

                if (images.isEmpty()) {
                    _exportResult.value = "此书籍中没有找到图片"
                } else {
                    // 模拟导出过程
                    for ((index, image) in images.withIndex()) {
                        // 模拟导出单个图片的过程
                        _progress.value = ((index + 1).toFloat() / images.size * 100).toInt()
                        kotlinx.coroutines.delay(100) // 模拟处理时间
                    }

                    _exportResult.value = "成功导出 ${images.size} 张图片"
                }

                _isExporting.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isExporting.value = false
            }
        }
    }

    /**
     * 导出日记条目中所有图片
     */
    fun exportImagesFromJournalEntry(journalEntryId: String) {
        viewModelScope.launch {
            try {
                _isExporting.value = true
                _error.value = null
                _exportResult.value = null
                _progress.value = 0

                val journalEntry = journalRepository.getJournalEntryById(journalEntryId)

                if (journalEntry == null) {
                    _exportResult.value = "未找到日记条目"
                } else {
                    val images = exportManager.extractImagesFromJournalEntry(journalEntry)

                    if (images.isEmpty()) {
                        _exportResult.value = "此日记中没有找到图片"
                    } else {
                        // 模拟导出过程
                        for ((index, image) in images.withIndex()) {
                            // 模拟导出单个图片的过程
                            _progress.value = ((index + 1).toFloat() / images.size * 100).toInt()
                            kotlinx.coroutines.delay(100) // 模拟处理时间
                        }

                        _exportResult.value = "成功导出 ${images.size} 张图片"
                    }
                }

                _isExporting.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isExporting.value = false
            }
        }
    }

    private fun convertJournalToMarkdown(journal: JournalEntry): String {
        val sb = StringBuilder()

        // Add title
        sb.append("# ${journal.title}\n\n")

        // Add metadata
        sb.append("*Created: ${journal.createdAt}*\n")
        sb.append("*Updated: ${journal.updatedAt}*\n")
        sb.append("*Visibility: ${journal.visibility}*\n\n")

        // Add tags if any
        if (journal.tags.isNotEmpty()) {
            sb.append("**Tags:** ${journal.tags.joinToString(", ")}\n\n")
        }

        // Add blocks
        for (block in journal.blocks) {
            when (block) {
                is com.pyy.journalapp.models.TextBlock -> {
                    sb.append("${block.text}\n\n")
                }
                is com.pyy.journalapp.models.HeadingBlock -> {
                    val hashes = "#".repeat(kotlin.math.min(block.level, 6)) // Max 6 levels
                    sb.append("$hashes ${block.text}\n\n")
                }
                is com.pyy.journalapp.models.QuoteBlock -> {
                    sb.append("> ${block.text}\n\n")
                }
                is com.pyy.journalapp.models.TodoBlock -> {
                    val status = if (block.completed) "x" else " "
                    sb.append("- [$status] ${block.text}\n")
                }
                is com.pyy.journalapp.models.ImageBlock -> {
                    sb.append("![Image](${block.imageId})\n")
                    if (!block.caption.isNullOrEmpty()) {
                        sb.append("*${block.caption}*\n")
                    }
                    sb.append("\n")
                }
                is com.pyy.journalapp.models.DividerBlock -> {
                    sb.append("---\n\n")
                }
            }
        }

        return sb.toString()
    }

    private suspend fun delayExportSimulation() {
        // Simulate some processing time for export
        kotlinx.coroutines.delay(1000)
    }
}