package com.example.demo103.ui.theme.log_workout

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogWorkoutViewModel (
    private val repository: WorkoutRepository
) : ViewModel(){
    private val _state = MutableStateFlow(LogWorkoutState())
    val state: StateFlow<LogWorkoutState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LogWorkoutUiEvent>()
    val uiEvent:  SharedFlow<LogWorkoutUiEvent> = _uiEvent.asSharedFlow()




    fun onEvent(event: LogWorkoutEvent){
        when(event){

            is LogWorkoutEvent.OnAddingSets -> {
                viewModelScope.launch {  //coroutine is stopped when the viewmodel is cleared

                    val weight = _state.value.currentWeight.toDoubleOrNull() //it convert the string into double if valid double if not return null
                    val reps = _state.value.currentReps.toIntOrNull()

                    if (weight == null|| reps == null ){
                        _uiEvent.emit(LogWorkoutUiEvent.SendSnackbar("invalid input"))
                        //emit error
                        return@launch // stop the current coroutine  and return to the caller
                    }

                    //create new set
                  val newEntry= WorkoutEntryEntity(
                      exerciseId = _state.value.exercise?.exerciseId ?: return@launch,
                            weight = weight,
                            reps = reps,
                            sets = _state.value.sets.size + 1,
                            date = System.currentTimeMillis()
                        )
                    // save the set to  db
                    repository.insertWorkoutEntry(newEntry)

                    _state.update {currentState-> //update the state
                        currentState.copy(  //do change only the sets and weight  and keep the rest of the state same
                            currentWeight = "", //reset the weight and reps after adding the set
                            currentReps="",
//                            sets=currentState.sets+newEntry // add the new set to the list of sets in the state (we can use + operator to add an element to a list and it will return a new list with the added element
                        )
                    }
                }
            }

            is LogWorkoutEvent.DeleteSet -> {
                viewModelScope.launch {
                    repository.deleteSetById(event.entryId)

                    _state.update { currentState->
                        currentState.copy(
                            sets=currentState.sets.filter { it.entryId != event.entryId  }
                        )

                    }
                    }
                }
           // viewmodelscope is not used here as it does not need ot touch the db
            is LogWorkoutEvent.UpdateReps -> {
                _state.update { currentState ->
                    currentState.copy(
                        currentWeight = event.reps
                    )
                }
            }

            is LogWorkoutEvent.UpdateWeight -> {
                _state.update{currentState->
                    currentState.copy(
                        currentWeight = event.weight
                    )
                }
            }

            is LogWorkoutEvent.SetExercise -> {
                _state.update {
                    it.copy(exercise = event.exercise) } // it here is the LogWorkoutState and we are copying the state and updating only the exercise field with the new exercise passed in the event
            }

          //since we already saving data to db with each set we dont need to save data to db again here
            is LogWorkoutEvent.SaveWorkout->{
                viewModelScope.launch {
                    if (_state.value.sets.isEmpty()){
                        _uiEvent.emit(LogWorkoutUiEvent.SendSnackbar("Atleast add 1 set"))
                         return@launch
                    }
                   else{
                       _uiEvent.emit(LogWorkoutUiEvent.NavBackToHome)
                    }
                }
            }
        }
    }








}