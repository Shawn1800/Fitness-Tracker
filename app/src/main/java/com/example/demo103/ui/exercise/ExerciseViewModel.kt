package com.example.demo103.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val repository : ExerciseRepository
): ViewModel() {

    val allExercises = repository.getAllExercises()
        .stateIn(viewModelScope, //convert flow to state flow so that compose can take in values
            SharingStarted.Lazily,emptyList())//

    fun addExercise(exercise: ExerciseEntity){  //launch coroutine to insert exercise
        //viewModelScope is a coroutine scope that is tied to the lifecycle of the ViewModel
        //it will be canceled when the ViewModel is cleared
        //viewModelscope is only use when it collects something
        viewModelScope.launch {
            repository.insertExercise(exercise)
        }
    }
//    viewmoelscope.launch is for:
//    insert
//    delete
//    update
//    fire-and-forget work

    fun searchExercise(searchQuery: String ): Flow<List<ExerciseEntity>> {
        return repository.searchExercises(searchQuery)
    }

    fun getExerciseByCategory(category:String ): Flow<List<ExerciseEntity>> {
            return repository.getExerciseByCategory(category)
}

    }


