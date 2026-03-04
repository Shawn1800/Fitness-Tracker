package com.example.demo103.ui.theme.log_workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo103.data.repository.WorkoutRepository

class LogWorkoutViewModelFactory (
    private val repository: WorkoutRepository
): ViewModelProvider.Factory
{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LogWorkoutViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LogWorkoutViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

