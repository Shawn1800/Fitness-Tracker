package com.example.demo103.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demo103.data.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECt*FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertExercise(exercise: ExerciseEntity)

//    @Delete
//    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT*FROM Exercises WHERE Exercise_Name LIKE '%' || :searchQuery || '%'")
    fun searchExercises(searchQuery: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE category= :category")
    fun getExerciseByCategory(category :String  ): Flow<List<ExerciseEntity>>

}