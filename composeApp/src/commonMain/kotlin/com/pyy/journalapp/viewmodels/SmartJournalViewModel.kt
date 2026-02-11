package com.pyy.journalapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyy.journalapp.ai.AISuggestionEngine
import com.pyy.journalapp.ai.ContentAnalysis
import com.pyy.journalapp.timemachine.TimeCapsuleManager
import com.pyy.journalapp.timemachine.TimeCapsule
import com.pyy.journalapp.timemachine.CapsuleStatus
import com.pyy.journalapp.templates.TemplateManager
import com.pyy.journalapp.templates.WritingContext
import com.pyy.journalapp.models.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class SmartJournalViewModel(
    private val aiSuggestionEngine: AISuggestionEngine = AISuggestionEngine(),
    private val timeCapsuleManager: TimeCapsuleManager = TimeCapsuleManager(),
    private val templateManager: TemplateManager = TemplateManager()
) : ViewModel() {

    private val _aiSuggestions = MutableStateFlow<ContentAnalysis?>(null)
    val aiSuggestions: StateFlow<ContentAnalysis?> = _aiSuggestions

    private val _timeCapsules = MutableStateFlow<List<TimeCapsule>>(emptyList())
    val timeCapsules: StateFlow<List<TimeCapsule>> = _timeCapsules

    private val _dueCapsules = MutableStateFlow<List<TimeCapsule>>(emptyList())
    val dueCapsules: StateFlow<List<TimeCapsule>> = _dueCapsules

    private val _writingContext = MutableStateFlow(WritingContext())
    val writingContext: StateFlow<WritingContext> = _writingContext

    /**
     * 分析日记内容并生成AI建议
     */
    fun analyzeContent(entry: JournalEntry) {
        viewModelScope.launch {
            val analysis = aiSuggestionEngine.analyzeContent(entry)
            _aiSuggestions.value = analysis
        }
    }

    /**
     * 创建时光胶囊
     */
    fun createTimeCapsule(entry: JournalEntry, targetDate: LocalDate) {
        viewModelScope.launch {
            val capsule = timeCapsuleManager.createCapsule(entry, targetDate)
            val currentCapsules = _timeCapsules.value.toMutableList()
            currentCapsules.add(capsule)
            _timeCapsules.value = currentCapsules
        }
    }

    /**
     * 检查到期的时光胶囊
     */
    fun checkDueCapsules(currentDate: LocalDate) {
        viewModelScope.launch {
            val dueCapsules = timeCapsuleManager.checkDueCapsules(_timeCapsules.value, currentDate)
            _dueCapsules.value = dueCapsules
        }
    }

    /**
     * 更新写作情境
     */
    fun updateWritingContext(context: WritingContext) {
        _writingContext.value = context
    }

    /**
     * 推荐情境化模板
     */
    fun recommendTemplate(content: String = ""): com.pyy.journalapp.templates.JournalTemplate {
        return templateManager.recommendTemplate(_writingContext.value, content)
    }

    /**
     * 获取即将到来的时光胶囊
     */
    fun getUpcomingCapsules(daysAhead: Int = 7): List<TimeCapsule> {
        return timeCapsuleManager.getUpcomingCapsules(_timeCapsules.value, daysAhead)
    }

    /**
     * 标记时光胶囊为已送达
     */
    fun markCapsuleAsDelivered(capsuleId: String) {
        val updatedCapsules = _timeCapsules.value.map { capsule ->
            if (capsule.id == capsuleId) {
                capsule.copy(status = CapsuleStatus.DELIVERED)
            } else {
                capsule
            }
        }
        _timeCapsules.value = updatedCapsules

        // 从到期列表中移除
        val updatedDueCapsules = _dueCapsules.value.filter { it.id != capsuleId }
        _dueCapsules.value = updatedDueCapsules
    }
}