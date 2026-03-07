package com.example.demo103.ui.theme.log_workout

sealed interface LogWorkoutUiEvent {
    data class SendSnackbar(val message:String) : LogWorkoutUiEvent
    data object SaveWorkout : LogWorkoutEvent


    object  NavBackToHome: LogWorkoutUiEvent
}