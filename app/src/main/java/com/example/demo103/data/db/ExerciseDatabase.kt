package com.example.demo103.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demo103.data.entity.ExerciseEntity
import com.example.demo103.data.entity.WorkoutEntryEntity
import com.example.demo103.data.dao.ExerciseDao
import com.example.demo103.data.dao.WorkoutEntryEntityDao

@Database(entities =
    [ExerciseEntity::class,
    WorkoutEntryEntity::class],
    version = 1,
    exportSchema = true)


abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract  fun workoutEntryEntityDao(): WorkoutEntryEntityDao



}