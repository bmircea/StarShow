package com.example.starshow

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface ObservationDao {
    @Insert
    fun insert(observation: Observation)

    @Delete
    fun delete(observation: Observation)

    @Update
    fun update(observation: Observation)

    @Query("SELECT * FROM Observation")
    fun getAllObservations(): List<Observation>



}