package com.example.demo103.ui.theme.exercise_selection

import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.ui.theme.log_workout.WorkoutViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow

data class ExerciseState (
    val exercises : List<ExerciseEntity> = emptyList(),
    val searchQuery :String="",
    val selectedCategory : String?=null,
    val isSearching : Boolean=false,
)