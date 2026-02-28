package com.example.demo103.data.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.data.dao.WorkoutEntryEntityDao
import com.example.demo103.data.entity.WorkoutWithExercise
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutEntryEntityDao: WorkoutEntryEntityDao) {
    suspend fun insertWorkoutEntry(entry : WorkoutEntryEntity){
        workoutEntryEntityDao.insertWorkoutEntry(entry)
    }
    fun getWorkoutByDate(date:Long): Flow<List<WorkoutWithExercise>> {
        return workoutEntryEntityDao.getWorkoutByDate(date)
    }
    fun getWorkoutByExercise(exerciseId: Int): Flow<List<WorkoutEntryEntity>> {
        return workoutEntryEntityDao.getWorkoutByExercise(exerciseId)
    }
    suspend fun deleteEntryById(id:Int) {
        workoutEntryEntityDao.deleteEntryById(id)
    }
}