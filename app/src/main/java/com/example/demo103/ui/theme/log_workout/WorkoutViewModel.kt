package com.example.demo103.ui.theme.log_workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WorkoutViewModel (private val repository: WorkoutRepository): ViewModel(){
    fun insertWorkoutEntry(entry: WorkoutEntryEntity ){
        viewModelScope.launch{
            repository.insertWorkoutEntry(entry )
        }
    }
//    fun getWorkoutByDate(date:Long): Flow<List<WorkoutEntryEntity>> {
//        return repository.getWorkoutByDate(date)
//    }
    fun getWorkoutByExerciseId(exerciseId:Int): Flow<List<WorkoutEntryEntity>> {
        return repository.getWorkoutByExercise(exerciseId)
    }
    fun deleteWorkoutById(id:Int){
        viewModelScope.launch{
            repository.deleteEntryById(id)
        }
    }
}