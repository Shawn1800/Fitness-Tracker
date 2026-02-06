package com.example.demo103.data.repository

import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.dao.ExerciseDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(
    private val exerciseDao: ExerciseDao) {

    fun getAllExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getAllExercises()

    suspend fun  insertExercise(exercise: ExerciseEntity)= exerciseDao.insertExercise(exercise)

    fun searchExercises(searchQuery: String): Flow<List<ExerciseEntity>> = exerciseDao.searchExercises(searchQuery)

    fun getExerciseByCategory(category :String  ): Flow<List<ExerciseEntity>> = exerciseDao.getExerciseByCategory(category)
}