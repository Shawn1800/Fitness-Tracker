package com.example.demo103.ui.theme.home


sealed interface HomeUiEvent {
    data object NavigateToExerciseSelection : HomeUiEvent
    //    data class EditExercise(val exercise: ExerciseEntity): ExerciseUiEvent

}