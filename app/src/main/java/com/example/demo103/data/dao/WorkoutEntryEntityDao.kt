package com.example.demo103.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demo103.data.entity.WorkoutEntryEntity
import kotlinx.coroutines.flow.Flow

//also called appContainer
@Dao
interface WorkoutEntryEntityDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkoutEntry(entry: WorkoutEntryEntity) //recheck

    @Query("SELECT * FROM workout_entry WHERE date = :date")
    fun getWorkoutForDate(date:Long): Flow<List<WorkoutEntryEntity>>

    @Query("SELECT * FROM workout_entry WHERE exercise_id = :exerciseId")
    fun getWorkoutByExercise(exerciseId: Int): Flow<List<WorkoutEntryEntity>>
    @Query("DELETE  FROM workout_entry WHERE entryId=:id")
    suspend fun deleteEntryById(id:Int)

}