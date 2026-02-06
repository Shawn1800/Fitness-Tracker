package com.example.demo103.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo103.data.repository.ExerciseRepository
import com.example.demo103.data.repository.WorkoutRepository
import com.example.demo103.ui.exercise.ExerciseViewModel

class WorkoutViewModelFactory (private val repository: WorkoutRepository
): ViewModelProvider.Factory

{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WorkoutViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

