package com.nourfadaa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nourfadaa.data.ChildEntity
import com.nourfadaa.data.ChildRepository
import com.nourfadaa.data.ContentRepository
import com.nourfadaa.domain.Lesson
import com.nourfadaa.domain.LessonReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LessonListViewModel(
    repo: ChildRepository,
    content: ContentRepository,
    childId: Long,
    sectionId: String
) : ViewModel() {
    val lessons: List<Lesson> = content.lessons(sectionId)

    val child: StateFlow<ChildEntity?> = repo.observeChild(childId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val done: StateFlow<Set<String>> = repo.observeProgress(childId)
        .map { list -> list.map { it.lessonId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
}

class LessonViewModel(
    private val repo: ChildRepository,
    content: ContentRepository,
    private val childId: Long,
    sectionId: String,
    lessonId: String
) : ViewModel() {
    val lesson: Lesson? = content.lesson(sectionId, lessonId)

    val child: StateFlow<ChildEntity?> = repo.observeChild(childId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _reward = MutableStateFlow<LessonReward?>(null)
    val reward: StateFlow<LessonReward?> = _reward.asStateFlow()

    fun complete() {
        val l = lesson ?: return
        viewModelScope.launch { _reward.value = repo.completeLesson(childId, l.id) }
    }
}
