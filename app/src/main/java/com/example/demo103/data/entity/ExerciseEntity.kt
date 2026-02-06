package com.example.demo103.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="exercises")
data class ExerciseEntity(

    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int = 0,  //id

    @ColumnInfo(name="exercise_name")
    val exerciseName: String,  //bench

    @ColumnInfo(name="category")
    val category:String //chest ,legs,
)