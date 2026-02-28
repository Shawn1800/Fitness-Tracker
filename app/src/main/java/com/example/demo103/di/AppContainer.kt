package com.example.demo103.di

import android.content.Context
import com.example.demo103.data.dao.ExerciseDao
import com.example.demo103.data.dao.WorkoutEntryEntityDao
import com.example.demo103.data.db.ExerciseDatabase
import com.example.demo103.data.repository.ExerciseRepository
import com.example.demo103.data.repository.WorkoutRepository

class AppContainer (context: Context){

    val database: ExerciseDatabase =ExerciseDatabase.getInstance(context)
//        Room.databaseBuilder(
//            context.applicationContext,
//            ExerciseDatabase::class.java,
//            "exercise_database"
//        ).build()

    val exerciseDao : ExerciseDao = database.exerciseDao()
    val workoutDao: WorkoutEntryEntityDao = database.workoutEntryEntityDao()

    val exerciseRepository= ExerciseRepository(exerciseDao)
    val workoutRepository = WorkoutRepository(workoutDao)

}