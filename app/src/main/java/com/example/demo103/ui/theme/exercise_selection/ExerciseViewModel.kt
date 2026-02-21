package com.example.demo103.ui.theme.exercise_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.repository.ExerciseRepository
import com.example.demo103.ui.theme.home.HomeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class)
class ExerciseViewModel(
    private val repository : ExerciseRepository
): ViewModel() {

// we dont use this here as we let the piplline handle search and category selection
    // it does not work with flatmaplates and combine
//    private val _exerciseState = MutableStateFlow(ExerciseState())
//    val exercise_state: StateFlow<ExerciseState> = _exerciseState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<String?>(null)
    private val _isSearching = MutableStateFlow(false)

    // ── UI State ──────────────────────────────────────────────────────────
// 1. Create a flow for the filtered exercises based on search and category

    @OptIn(ExperimentalCoroutinesApi::class)
    private val filteredExercises = combine(_searchQuery.debounce(300), _selectedCategory) { query, category ->
        query to category
    }.onEach {
        _isSearching.value = true
    }.flatMapLatest { (query, category) ->
        val flow = when {
            query.isNotBlank() && category != null -> repository.searchExerciseByCategory(query, category)
            query.isNotBlank() -> repository.searchExercises(query)
            category != null -> repository.getExerciseByCategory(category)
            else -> repository.getAllExercises()
        }
        flow.onEach { _isSearching.value = false }
    }

    // 2. Final UI State: Combine all triggers into a single StateFlow
    @OptIn(FlowPreview::class)
    val state: StateFlow<ExerciseState> = combine(
        _searchQuery,
        _selectedCategory,
        _isSearching,
        filteredExercises
    ) { query, category, searching, exercises ->
        ExerciseState(
            exercises = exercises,
            searchQuery = query,
            selectedCategory = category,
            isSearching = searching
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExerciseState()
    )


    fun onEvent(event: ExerciseEvent) {
        when (event) {
            is ExerciseEvent.OnSearchQueryChange -> {
                _searchQuery.value = event.query
            }
            is ExerciseEvent.OnSelectCategory -> {
                _selectedCategory.value = event.category
            }
            is ExerciseEvent.OnClearCategory -> _selectedCategory.value = null
        }
    }


    }


