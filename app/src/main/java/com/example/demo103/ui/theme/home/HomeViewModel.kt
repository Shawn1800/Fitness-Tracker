package com.example.demo103.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.demo103.data.entity.WorkoutEntryEntity

import com.example.demo103.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    // Currently selected date (timestamp, e.g., start of day)
    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()

    // Workouts for the selected date
    val workoutsForSelectedDate: StateFlow<List<WorkoutEntryEntity>> =
        _selectedDate
            .filterNotNull()
            .flatMapLatest { date ->
                repository.getWorkoutByDate(date)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    // Called from HomeFragment when user picks a date from calendar
    fun onDateSelected(date: Long) {
        _selectedDate.value = date
    }

    // Add a new entry (called after logging weight/reps, if you want to insert directly)
    fun addEntry(entry: WorkoutEntryEntity) {
        viewModelScope.launch {
            repository.insertWorkoutEntry(entry)
        }
    }

    // Delete an entry from the list
    fun deleteEntry(id: Int) {
        viewModelScope.launch {
            repository.deleteEntryById(id)
        }
    }
}
