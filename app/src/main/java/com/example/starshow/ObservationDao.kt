package com.example.starshow

import androidx.room.*

@Dao
interface ObservationDao {
    @Insert
    fun insert(observation: Observation)

    @Delete
    fun delete(observation: Observation)

    @Update
    fun update(observation: Observation)

    @Query("SELECT * FROM observation")
    fun getAllObservations(): List<Observation>



}