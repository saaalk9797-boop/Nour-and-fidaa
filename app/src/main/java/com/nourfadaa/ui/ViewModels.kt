package com.nourfadaa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nourfadaa.data.ChildEntity
import com.nourfadaa.data.ChildRepository
import com.nourfadaa.domain.LessonReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VmFactory(private val build: () -> ViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = build() as T
}

class ChildPickerViewModel(repo: ChildRepository) : ViewModel() {
    val children: StateFlow<List<ChildEntity>> = repo.observeChildren()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class ChildHomeViewModel(repo: ChildRepository, childId: Long) : ViewModel() {
    val child: StateFlow<ChildEntity?> = repo.observeChild(childId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}

class SectionViewModel(
    private val repo: ChildRepository,
    private val childId: Long,
    private val sectionId: String
) : ViewModel() {
    val child: StateFlow<ChildEntity?> = repo.observeChild(childId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _reward = MutableStateFlow<LessonReward?>(null)
    val reward: StateFlow<LessonReward?> = _reward.asStateFlow()

    fun completeDemoLesson() {
        viewModelScope.launch { _reward.value = repo.completeLesson(childId, "demo-$sectionId") }
    }
}
