package com.example.demo103.ui.theme.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.data.repository.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class HomeViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()  //make it read only
    //SharedFlow is a hot flow that emits events to its collectors. It doesn't hold any values until a collector is attached, and it can be configured to replay a specific number of values to new collectors.
    private val _uiEvent =
        MutableSharedFlow<HomeUiEvent>() //shared flow does not hold state and reemit old values
    val uiEvent =
        _uiEvent.asSharedFlow()  //shared flow doesn't replay old value unlike stateflow better suited for fire and forget events
    private var workoutJob: Job? = null

    init {
        // initial date = today
        val today = LocalDate.now()
        onEvent(HomeEvent.OnDateSelected(today))
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnDateSelected -> {
                val millis = event.date
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli() //db stores date as long , so we convert to long
                _state.update { //automatically reads new state value and emits new value
                    it.copy( //"it " is the current homeState . copy() method provides a convenient way to create a slightly modified version of the current state without changing the original object.
                        selectedDate = event.date,  //so you update the state.value
                        selectedDateMillis = millis //we update the long value
                    )
                }
                observeWorkoutsForDate(millis)
            }

            is HomeEvent.OnAddWorkoutClick -> {
                viewModelScope.launch { //Emitting to SharedFlow is a suspend operation, so we launch a coroutine scoped to the ViewModel lifecycle.
                    _uiEvent.emit(HomeUiEvent.NavigateToExerciseSelection) //emit is a suspend , so it nedd a coroutine to work ,viewmodel scope is aslo lifecycle safe
                }       //suspend is about how code executes
                // Scope is about how long code is allowed to live
            }
        }
    }


    fun addExerciseToHome(exercise: ExerciseEntity) {
        val dateMillis = state.value.selectedDateMillis ?: return
        viewModelScope.launch {
            repository.insertWorkoutEntry(
                WorkoutEntryEntity(
                    exerciseId = exercise.exerciseId,
                    weight = 0.0,
                    reps = 0,
                    date = dateMillis,
                    sets = 1
                )
            )
        }
    }

    //Whenever the user selects a date, observe the database for workouts of that date and keep the UI updated in real time.
    //might use faltmaplatest if we want to cancel the previous flow collection when a new date is selected and only observe the latest date's workouts. But here we are manually canceling the previous job before launching a new one, which achieves the same result.
    private fun observeWorkoutsForDate(dateMillis: Long) {
        workoutJob?.cancel()  //canceling previous coroutine .Cancel the previous Flow collection so only the currently selected date is observed.
        workoutJob =
            viewModelScope.launch {// Launch a lifecycle-aware coroutine that collects workouts for the selected date.
                repository.getWorkoutByDate(dateMillis)
                    .collect { workouts ->  // collect means observe the stream and when new value is emmited runt this block
                        _state.update { // reads current ui state , created new state,emits it to the observers
                            it.copy( //create a slightly modified version of the state
                                workouts = workouts //upd ate the workouts in the state .Database → Flow → ViewModel → State → UI
                            )
                        }
                    }
            }
    }
}
