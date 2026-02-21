package com.example.demo103.ui.theme.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.demo103.data.entity.WorkoutEntryEntity

import com.example.demo103.data.repository.WorkoutRepository
import com.example.demo103.ui.theme.exercise_selection.ExerciseEvent
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
    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
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
                    .toEpochMilli()

                _state.update {
                    it.copy(
                        selectedDate = event.date,
                        selectedDateMillis = millis
                    )
                }

                observeWorkoutsForDate(millis)
            }
            is HomeEvent.OnAddWorkoutClick -> {
                viewModelScope.launch {
                    _uiEvent.emit(HomeUiEvent.NavigateToExerciseSelection)
                }

            }
    }
}
    private fun observeWorkoutsForDate(dateMillis: Long) {
        workoutJob?.cancel()
        workoutJob = repository.getWorkoutByDate(dateMillis)
            .onEach { workouts ->
                _state.update { it.copy(workouts = workouts) }
            }
            .launchIn(viewModelScope)
    }


}
