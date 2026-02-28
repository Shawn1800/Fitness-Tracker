package com.example.demo103.ui.theme.exercise_selection

import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.entity.WorkoutEntryEntity

sealed interface ExerciseUiEvent {
    data class  AddExercise(val exercise: ExerciseEntity): ExerciseUiEvent
    object NavigateBack: ExerciseUiEvent

//    data class ExerciseSelected(
//        val exercise: ExerciseEntity
//    ) : ExerciseUiEvent

}