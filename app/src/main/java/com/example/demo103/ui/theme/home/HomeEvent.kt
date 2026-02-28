package com.example.demo103.ui.theme.home

import com.example.demo103.data.entity.ExerciseEntity
import java.time.LocalDate

sealed interface HomeEvent {
    data class OnDateSelected(val date: LocalDate) : HomeEvent // data class is used when the event carries info
    object OnAddWorkoutClick : HomeEvent  // fab  // here object is used when the event carries no data

    //______________to add set in the workoutcard _________________________//
    data class OnExerciseAdd(val exercise: ExerciseEntity): HomeEvent
    // events while editing workout cards
    data class OnAddSet(val exerciseId: Int ) : HomeEvent
    //On changing the value of reps and weight
    data class OnSetValueChange(
        val exerciseId: Int,
        val setIndex: Int,
        val reps : Int,
        val weight: Double
    )    : HomeEvent

    //On Clicking save on the card
    object OnSaveWorkoutClick: HomeEvent

}