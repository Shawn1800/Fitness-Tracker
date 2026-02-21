package com.example.demo103.ui.theme.home

import java.time.LocalDate

sealed interface HomeEvent {
    data class OnDateSelected(val date: LocalDate) : HomeEvent
    data object OnAddWorkoutClick : HomeEvent  // fab
}