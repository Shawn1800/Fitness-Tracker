package com.example.demo103.ui.theme.log_workout

import com.example.demo103.data.entity.WorkoutEntryEntity

sealed interface LogWorkoutEvent {
    data class  OnAddingSets(val exerciseId : Int) : LogWorkoutEvent
    data class UpdateWeight (val setId:Int, val weight : String): LogWorkoutEvent
    data class UpdateReps (val setId:Int ,val reps : String): LogWorkoutEvent
    data class DeleteSet (val entryId : Int): LogWorkoutEvent



}