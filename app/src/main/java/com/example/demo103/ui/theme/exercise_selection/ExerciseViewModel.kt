package com.example.demo103.feature.exercise_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.repository.ExerciseRepository
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

class ExerciseViewModel(
    private val repository : ExerciseRepository
): ViewModel() {
// ui input
    private val searchQuery = MutableStateFlow("")

    private val selectedCategory = MutableStateFlow<String?>(null)
// ui state
    private val _isSearching = MutableStateFlow(false) //
    
    val isSearching : StateFlow<Boolean> = _isSearching.asStateFlow() //to create state flow from mutable state flow, so that compose can take in values

    val exercises : StateFlow<List<ExerciseEntity>> =
        combine(
            searchQuery.debounce(300),
            selectedCategory
        ) { query, category ->
            query to category
        }
            .onEach { _isSearching.value=true }
            .flatMapLatest { (query,category)->
                when{

                    query.isNotBlank() && category != null ->
                        repository.searchExerciseByCategory(query, category)

                    query.isNotBlank() ->
                        repository.searchExercises(query)

                    category != null ->
                        repository.getExerciseByCategory(category)

                    else ->
                        repository.getAllExercises()
                }
            }
            .onEach { _isSearching.value = false }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )


//
//    fun clearSelection() {
//        _selectedExercise.value = null
//    }



    fun searchExercise(query: String ): Flow<List<ExerciseEntity>> {
        return repository.searchExercises(query)
    }

    fun getExerciseByCategory(category:String ): Flow<List<ExerciseEntity>> {
            return repository.getExerciseByCategory(category)
}

    }


