package com.example.demo103.ui.theme.exercise_selection

import com.example.demo103.data.entity.ExerciseEntity

data class ExerciseState (
    val exercises : List<ExerciseEntity> = emptyList(),
    val searchQuery :String="",
    val selectedCategory : String?=null,
    val isSearching : Boolean=false,
)