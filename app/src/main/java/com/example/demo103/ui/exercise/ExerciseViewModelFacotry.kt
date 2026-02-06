package com.example.demo103.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demo103.data.repository.ExerciseRepository

//A Factory helps recreate the ViewModel with the same parameters
// after a configuration change, ensuring seamless user experience

class ExerciseViewModelFacotry (
    private val repository: ExerciseRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}